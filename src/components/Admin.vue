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
        <el-input
          v-model="searchModel.nameLike"
          placeholder="请输入用户组名称"
        />
      </el-form-item>
    </el-form>
  </div>
  <!-- 按钮区 -->
  <div class="action">
    <el-button
      color="#626aef"
      :icon="CirclePlusFilled"
      @click="addAdminClick"
      plain
    >
      新建用户</el-button
    >
    <el-button color="#626aef" :icon="Edit" @click="editAdminClick" plain>
      修改用户组</el-button
    >
    <el-button color="#626aef" :icon="Edit" @click="editAdminMsgClick" plain>
      修改用户</el-button
    >
    <el-button color="#626aef" :icon="Search" @click="search" plain>
      查询</el-button
    >
    <el-button color="#626aef" :icon="Refresh" @click="resetFrom" plain>
      重置</el-button
    >
    <el-button type="danger" :icon="Delete" @click="deleteAdminClick" plain>
      删除用户</el-button
    >
  </div>
  <!-- 数据显示区 -->
  <div class="data-grid">
    <!-- 数据表格 -->
    <el-table
      row-key="name"
      :data="admins"
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
      <el-table-column prop="name" label="用户组名称" width="140" />
      <el-table-column prop="groupList" label="用户组" width="200">
        <template #default="scope">
          <el-popover
            effect="light"
            trigger="hover"
            placement="top"
            width="auto"
          >
            <template #default>
              <div v-for="item in scope.row.groupList">
                Group: id:{{ item.id }},name:{{ item.name }}
              </div>
            </template>
            <template #reference>
              <el-tag>权限信息</el-tag>
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
    :title="'修改用户组用户组'"
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
          placeholder="请输入管理员名称"
          autocomplete="off"
        />
      </el-form-item>
      <el-form-item label="groupList" prop="groupList">
        <el-tree
          ref="treeGroupList"
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
<!-- 添加修改对话框 -->
  <el-dialog
    v-model="dialogAddFormVisible"
    :draggable="true"
    :title="diaglogFormTitle"
    width="500"
    @close="diaglogClose"
  >
    <el-form
      :model="formModel"
      :label-width="100"
      ref="auformRef"
      :rules="rules"
    >
      <el-form-item label="name" prop="name">
        <el-input
          v-model="formModel.name"
          placeholder="请输入管理员名称"
          autocomplete="off"
        />
      </el-form-item>
      <el-form-item label="phone" prop="phone">
        <el-input
          v-model="formModel.phone"
          placeholder="请输入管理员手机号"
          autocomplete="off"
        />
      </el-form-item>
      <el-form-item label="password" prop="password">
        <el-input
          v-model="formModel.password"
          placeholder="请输入管理员密码"
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
  deleteAdminRequest,
  save,
  updateAdmin,
  update,
  getAllGroup,
} from "@/api/AdminApi";
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
const admins = ref([]);

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
    admins.value = resp.data;
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
function deleteAdminClick() {
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
      deleteAdmins(ids);
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
async function deleteAdmins(ids) {
  let resp = await deleteAdminRequest(ids);
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

// 添加修改模型，添加只需要name，修改需要id,name groupList
let formModel = reactive({
  id: "",
  name: "",
  phone: "",
  password: "",
  groupList: [],
});
// 对话框关闭的时候调用此函数清空对话框表单
function diaglogClose() {
  dialogAddFormVisible.value = false;
  dialogFormVisible.value = false;
  selectedIdsGroup = [];
  if (treeGroupList.value) {
    treeGroupList.value.setCheckedKeys(selectedIdsGroup);
  }

  Object.assign(formModel, {
    id: "",
    name: "",
    phone: "",
    password: "",
    groupList: [],
  });
}
// 添加修改用户对话框，修改对话框实例
let auformRef = ref();

// 对话框设置
let dialogAddFormVisible = ref(false);
let diaglogFormTitle = ref();
// 显示添加对话框
function addAdminClick() {
  diaglogFormTitle="Add Admin";
  dialogAddFormVisible.value = true;
}
// 显示修改用户信息对话框
function editAdminMsgClick() {
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
    dialogAddFormVisible.value = true;
    diaglogFormTitle = "Edit Admin";
    // NOTE:3.把用户组信息存储上权限树数据(这里没办法把原来的用户组也带上了)
    Object.assign(formModel, lodash.cloneDeep(rows[0]));
    formModel.password = "";
  }
}
// 添加修改用户事件
async function doSubmit() {
  auformRef.value.validate(async (valid) => {
    if (valid) {
      let admin = toRaw(formModel);
      let resp= "";
      if(admin.id && admin.id!==''){
        console.log(admin);
        resp = await updateAdmin(admin); 
      }else{
        resp = await save(admin);
      }
      console.log(resp);
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

// 修改权限表单实例
let uformRef = ref();
// 修改权限对话框实例
let dialogFormVisible = ref(false);
// 权限树 实例
let treeGroupList = ref();
// 修改权限树 参数
const props = {
  label: "name",
  isLeaf: true,
};
// 目前选中的权限项(初始化状态为选中的项的已有权限)
let selectedIdsGroup = reactive();
// 设置权限回显
function fillInitialGroup(groupList) {
  let ids = [];
  groupList.forEach((it) => ids.push(it.id));
  selectedIdsGroup = ids;
  treeGroupList.value.setCheckedKeys(ids);
}
// 提交前获取当前选中的权限
function getSelectedGroup() {
  selectedIdsGroup = treeGroupList.value.getCheckedKeys();
}
// 修改用户组对话框
let tmpLoadData = reactive();
async function editAdminClick() {
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
    tmpLoadData = (await getAllGroup()).data;
    dialogFormVisible.value = true;
    await nextTick();
    // NOTE:2.第二步进行选项回填
    fillInitialGroup(toRaw(rows[0].groupList));
    // NOTE:3.把用户组信息存储上权限树数据(这里没办法把原来的用户组也带上了)
    Object.assign(formModel, lodash.cloneDeep(rows[0]));
  }
}
// 修改事件
async function doEdit() {
  uformRef.value.validate(async (valid) => {
    if (valid) {
      getSelectedGroup();
      let admin = toRaw(formModel);
      admin.groupList = [];
      let resp = await update(admin, toRaw(selectedIdsGroup));
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
  name: [{ required: true, message: "请输入用户名称", trigger: "blur" }],
};

//当页面就绪之后
onMounted(() => {
  search(); //异步函数
});
</script>