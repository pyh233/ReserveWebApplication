<template>
  <!-- 搜索区 -->
  <div class="search">
    <!-- 表单 -->
    <el-form :inline="true" :model="searchModel" ref="searchFromRef">
      <!-- 表单元素 -->
      <el-form-item label="主键:" prop="id">
        <el-input v-model="searchModel.id" placeholder="请输入主键" />
      </el-form-item>

      <el-form-item label="名称:" prop="nameLike">
        <el-input v-model="searchModel.nameLike" placeholder="请输入权限名称" />
      </el-form-item>
    </el-form>
  </div>
  <!-- 按钮区 -->
  <div class="action">
    <el-button
      color="#626aef"
      :icon="CirclePlusFilled"
      @click="addPermissionClick"
      plain
    >
      新建权限</el-button
    >
    <el-button color="#626aef" :icon="Edit" @click="editPermissionClick" plain>
      修改权限资源</el-button
    >
    <el-button color="#626aef" :icon="Search" @click="search" plain>
      查询权限</el-button
    >
    <el-button color="#626aef" :icon="Refresh" @click="resetFrom" plain>
      重置</el-button
    >
    <el-button
      type="danger"
      :icon="Delete"
      @click="deletePermissionClick"
      plain
    >
      删除权限</el-button
    >
  </div>
  <!-- 数据显示区 -->
  <div class="data-grid">
    <!-- 数据表格 -->
    <el-table
      row-key="name"
      :data="permissions"
      :stripe="true"
      :border="true"
      :highlight-current-row="true"
      @row-click="tableRowClick"
      v-loading="loading"
      ref="tableRef"
      style="width: 100%"
    >
      <!-- 列设置 -->
      <el-table-column type="selection" width="60" />
      <el-table-column prop="id" label="主键" width="60" />
      <el-table-column prop="name" label="权限名称" width="140" />
      <el-table-column prop="routeList" label="资源" width="200">
        <template #default="scope">
          <el-popover
            effect="light"
            trigger="hover"
            placement="top"
            width="auto"
          >
            <template #default>
              <div v-for="item in scope.row.routeList">Route: {{ item }}</div>
            </template>
            <template #reference>
              <el-tag>资源信息</el-tag>
            </template>
          </el-popover>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页条组件 -->
    <el-pagination
      class="pi"
      layout="total, prev, pager, next, sizes, jumper "
      :total="pi.total"
      v-model:current-page="pi.page"
      v-model:page-size="pi.limit"
      :page-sizes="[10, 15, 30, 50, 100]"
      @change="search"
    />
  </div>

  <!-- 修改对话框 -->
  <el-dialog
    v-model="dialogFormVisible"
    :draggable="true"
    :title="'修改权限资源'"
    width="500"
    @close="diaglogClose"
  >
    <el-form
      :model="formModel"
      :label-width="100"
      ref="uformRef"
      :rules="rules"
    >
      <el-form-item label="name" prop="name">
        <el-input
          v-model="formModel.name"
          placeholder="请输入权限名称"
          autocomplete="off"
        />
      </el-form-item>
      <el-form-item label="routeList" prop="routeList">
        <el-tree
          ref="treeRouteList"
          style="max-width: 600px"
          :data="tmpLoadData"
          :props="props"
          :load="loadNode"
          node-key="id"
          show-checkbox
          :check-on-click-node="true"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="diaglogClose">Cancel</el-button>
        <el-button type="primary" @click="doEdit">Confirm</el-button>
      </div>
    </template>
  </el-dialog>

  <el-dialog
    v-model="dialogAddFormVisible"
    :draggable="true"
    :title="'添加权限'"
    width="500"
    @close="diaglogClose"
  >
    <el-form
      :model="formModel"
      :label-width="100"
      ref="aformRef"
      :rules="rules"
    >
      <el-form-item label="name" prop="name">
        <el-input
          v-model="formModel.name"
          placeholder="请输入权限名称"
          autocomplete="off"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="diaglogClose">Cancel</el-button>
        <el-button type="primary" @click="doSubmit">Confirm</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<style scoped>
.data-grid {
  margin-top: 15px;
}

.pi {
  margin-top: 15px;
}
</style>

<script setup>
import { ref, reactive, onMounted, toRaw, nextTick } from "vue";
import {
  Search,
  Edit,
  Delete,
  CirclePlusFilled,
  Refresh,
} from "@element-plus/icons-vue";
import {
  findAll,
  deletePermissionRequest,
  save,
  update,
  getAllRoute,
} from "@/api/PermissionApi";
import { ElMessage, ElMessageBox } from "element-plus";
import lodash from "lodash";
const loading = ref(true);
//分页数据
let pi = ref({
  page: 1,
  limit: 10,
  total: 0,
});
//所有用户数据
const permissions = ref([]);

//搜索表单数据模型
const searchModel = reactive({
  id: "",
  nameLike: "",
});

// 搜索表单实例
const searchFromRef = ref();
// 重置按钮事件
function resetFrom() {
  searchFromRef.value.resetFields();
}
//查询全部用户
async function search() {
  loading.value = true;
  //转换成原始格式
  let params = toRaw(searchModel);
  let resp = await findAll(pi.value.page, pi.value.limit, params);
  if (resp.success && resp.data.length > 0) {
    permissions.value = resp.data;
    pi.value = {
      total: resp.pageInfo.total,
      page: resp.pageInfo.pageNum,
      limit: resp.pageInfo.pageSize,
    };
  }
  loading.value = false;
}

// 表格实例
let tableRef = ref();
// 表格行点击事件
function tableRowClick(row) {
  tableRef.value.toggleRowSelection(row);
}
// 点击删除
// TODO:存在一个删除全页面的情况
function deletePermissionClick() {
  let rows = tableRef.value.getSelectionRows();
  let ids = [];
  rows.forEach((row) => {
    ids.push(row.id);
  });
  if (ids.length == 0) {
    ElMessage({
      message: "请选择要删除的行.",
      type: "error",
    });
    return;
  }
  ElMessageBox.confirm("Will Delete Selected Rows,Continue?", "Warning", {
    confirmButtonText: "OK",
    cancelButtonText: "Cancel",
    type: "warning",
  })
    .then(() => {
      deletePermissions(ids);
      // ---------------------------------------------------
    })
    .catch(() => {
      ElMessage({
        type: "info",
        message: "Delete canceled",
      });
    });
}
// 删除事件
async function deletePermissions(ids) {
  let resp = await deletePermissionRequest(ids);
  if (resp.success) {
    ElMessage({
      type: "success",
      message: resp.message,
    });
    search();
  } else {
    ElMessage({
      type: "error",
      message: resp.message,
    });
  }
  // return resp;
}

// 添加修改模型，添加只需要name，修改需要id,name routeList
let formModel = reactive({
  id: "",
  name: "",
  routeList: [],
});
// 添加对话框，修改对话框实例
let aformRef = ref();
let uformRef = ref();

// 对话框关闭的时候调用此函数清空对话框表单
async function diaglogClose() {
  dialogAddFormVisible.value = false;
  dialogFormVisible.value = false;
  selectedIdsRoute = [];
  if (treeRouteList.value) {
    treeRouteList.value.setCheckedKeys(selectedIdsRoute);
  }
  Object.assign(formModel, {
    id: "",
    name: "",
    routeList: [],
  });
}

// 对话框设置
let dialogAddFormVisible = ref(false);
let dialogFormVisible = ref(false);
// 显示添加对话框
function addPermissionClick() {
  dialogAddFormVisible.value = true;
}
// 添加权限
async function doSubmit() {
  aformRef.value.validate(async (valid) => {
    if (valid) {
      let permission = toRaw(formModel);
      let resp = await save(permission);
      if (resp.success) {
        ElMessage({
          type: "success",
          message: resp.message,
        });
        search();
        dialogAddFormVisible.value = false;
      } else {
        // 实际上到不了这个地方，因为失败返回的是一个badRequest
        ElMessage({
          type: "error",
          message: resp.message,
        });
      }
    } else {
      ElMessage({
        type: "error",
        message: "请检查您的数据后再尝试",
      });
    }
  });
}

// 资源树 实例
let treeRouteList = ref();
// 修改资源树 参数
const props = {
  label: "name",
  isLeaf: true,
};
// 目前选中的资源项(初始化状态为选中的项的已有资源)
let selectedIdsRoute = reactive();
// 设置资源回显
function fillInitialRoute(routes) {
  let ids = [];
  routes.forEach((it) => ids.push(it.id));
  selectedIdsRoute = ids;
  treeRouteList.value.setCheckedKeys(ids);
}
// 提交前获取当前选中的资源
function getSelectedRoute() {
  selectedIdsRoute = treeRouteList.value.getCheckedKeys();
}
// 修改对话框
let tmpLoadData = reactive();
async function editPermissionClick() {
  let rows = tableRef.value.getSelectionRows();
  if (rows.length > 1) {
    ElMessage({
      type: "warning",
      message: "一次仅支持修改一行",
    });
  } else if (rows.length === 0) {
    ElMessage({
      type: "warning",
      message: "请选择要修改的行",
    });
  } else {
    // NOTE:1.首先应该进行进行数据请求，渲染数据
    tmpLoadData = (await getAllRoute()).data;
    dialogFormVisible.value = true;
    await nextTick();
    // NOTE:2.第二步进行选项回填
    fillInitialRoute(toRaw(rows[0].routeList));
    // NOTE:3.把权限信息存储上资源树数据(这里没办法把原来的权限也带上了)
    Object.assign(formModel, lodash.cloneDeep(rows[0]));
  }
}
// 修改事件
async function doEdit() {
  uformRef.value.validate(async (valid) => {
    if (valid) {
      getSelectedRoute();
      let permission = toRaw(formModel);
      permission.routeList = [];
      let resp = await update(permission, toRaw(selectedIdsRoute));
      if (resp.success) {
        ElMessage({
          type: "success",
          message: resp.message,
        });
        search();
        dialogFormVisible.value = false;
      } else {
        ElMessage({
          type: "error",
          message: resp.message,
        });
      }
    } else {
      ElMessage({
        type: "error",
        message: "请检查您的数据后再尝试",
      });
    }
  });
}
// 表单验证规则
const rules = {
  name: [{ required: true, message: "请输入权限名称", trigger: "blur" }],
};

//当页面就绪之后
onMounted(() => {
  search(); //异步函数
});
</script>