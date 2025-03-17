<template>
  <div class="common-layout">
    <el-container class="layout">
      <el-header class="header" height="60px">
        <el-page-header icon="Grid">
          <template #content>
            <div class="flex items-center">
              <el-avatar
                :size="32"
                class="mr-3"
                :src="'/static/example/pic1.jpg'"
              />
              <span class="text-large font-600 mr-3"> 心理辅导预约管理系统 </span>
              <span
                class="text-sm mr-2"
                style="color: var(--el-text-color-regular)"
              >
                ------用户权限管理系统
              </span>
              <el-tag><p v-text="router.currentRoute.value.name"></p></el-tag>
            </div>
          </template>
          <template #extra>
            <div class="flex items-center">
              <el-button @click="logoutConfirm">Print</el-button>
              <el-button type="primary" class="ml-2" @click="logoutConfirm"
                >Edit</el-button
              >
            </div>
          </template>
        </el-page-header>
      </el-header>
      <el-divider></el-divider>
      <el-container class="content">
        <!-- 侧边导航 -->
        <el-aside class="aside" width="200px">
          <el-menu
            :default-active="defaultMainUrl"
            class="el-menu-vertical-demo"
            :router="true"
          >
            <el-sub-menu
              v-for="mi in menuItems"
              :key="mi.index"
              :index="'menu' + mi.index"
            >
              <template #title>
                <el-icon>
                  <component :is="mi.icon" />
                </el-icon>
                <span>{{ mi.title }}</span>
              </template>
              <el-menu-item
                v-for="ci in mi.children"
                :key="ci.index"
                :index="ci.url"
              >
                <el-icon>
                  <component :is="ci.icon" />
                </el-icon>
                {{ ci.title }}
              </el-menu-item>
            </el-sub-menu>
          </el-menu>
        </el-aside>
        <el-container>
          <el-main class="main">
            <!-- 二级路由组件 -->
            <router-view></router-view>
          </el-main>
          <el-footer class="footer"></el-footer>
        </el-container>
      </el-container>
    </el-container>
  </div>
</template>
<style scoped>
.common-layout {
  height: 100%;
}

.layout {
  height: 100%;
}

.header {
  color: black;
  line-height: 60px;
  align-content: center;
}

.content {
  height: calc(100% - 60px);
}

.main {
  --el-main-padding: 14px;
  color: #333;
  background: url("@/assets/vue.svg") no-repeat center;
}

.footer {
  color: #fff;
  line-height: 60px;
}

.el-menu-vertical-demo:not(.el-menu--collapse) {
  --el-menu-bg-color: #fff;
  --el-menu-text-color: black;
  --el-menu-active-color: #7970f3;
  width: 200px;
  min-height: 400px;
}
</style>
<script lang="ts" setup>
import { onMounted, reactive, ref } from "vue";
import { adminLogout } from "@/api/AdminLoginApi";
import { get, remove } from "@/util/token";
import {
  Document,
  Menu as IconMenu,
  Location,
  Setting,
  User,
  UserFilled,
  Calendar,
  CopyDocument,
  Clock,
  Lock,
} from "@element-plus/icons-vue";
import { useRouter } from "vue-router";
import { ElMessage, ElMessageBox } from "element-plus";
const router = useRouter();
let defaultMainUrl = ref("/");
onMounted(() => {
  // NOTE:保证左侧导航刷新后保持状态
  defaultMainUrl.value = router.currentRoute.value.fullPath;
});
// 导航菜单
const menuItems = [
  {
    index: "1",
    title: "用户管理",
    icon: User,
    children: [
      { index: "1-1", title: "用户信息管理", icon: Document, url: "/user/msg" },
      {
        index: "1-3",
        title: "用户预约管理",
        icon: Clock,
        url: "/user/reserve",
      },
    ],
  },
  {
    index: "2",
    title: "专家管理",
    icon: UserFilled,
    children: [
      {
        index: "2-1",
        title: "专家信息管理",
        icon: Document,
        url: "/expert/msg",
      },
      {
        index: "2-2",
        title: "专家课程管理",
        icon: Setting,
        url: "/expert/course",
      },
    ],
  },
  {
    index: "3",
    title: "课程管理",
    icon: Calendar,
    children: [
      { index: "3-1", title: "课程信息管理", icon: Document, url: "/course" },
    ],
  },
  {
    index: "4",
    title: "房间管理",
    icon: Location,
    children: [
      { index: "4-1", title: "房间信息管理", icon: Document, url: "/room" },
    ],
  },
  {
    index: "5",
    title: "权限管理",
    icon: Lock,
    children: [
      { index: "5-1", title: "用户权限", icon: Setting, url: "/admin" },
      { index: "5-2", title: "用户组权限", icon: Setting, url: "/admin/group" },
      { index: "5-3", title: "角色权限", icon: Setting, url: "/role" },
      { index: "5-4", title: "权限管理", icon: Setting, url: "/permission" },
      { index: "5-5", title: "资源管理", icon: Setting, url: "/route" },
    ],
  },
];

//登出
async function logoutConfirm() {
  ElMessageBox.confirm("即将登出,确认?", {
    confirmButtonText: "确认",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(async () => {
      let resp = await adminLogout(get());
      remove();
      console.log(resp);
      if (resp.data.success) {
        ElMessage.success(resp.data.message);
        setTimeout(() => {
          location.href = "/login";
        }, 2000);
      }else{
        ElMessage.error(resp.data.message);
      }
    })
    .catch(() => {
      console.log(router);
    });
}
</script>