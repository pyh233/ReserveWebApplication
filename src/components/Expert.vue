<template>
  <!-- 搜索区 -->
  <div class="search">
    <!-- 表单 -->
    <el-form :inline="true" :model="searchModel" ref="searchFromRef">
      <!-- 表单元素 -->
      <el-form-item label="主键:" prop="id">
        <el-input v-model="searchModel.id" placeholder="请输入用户主键" />
      </el-form-item>

      <el-form-item label="姓名:" prop="name">
        <el-input v-model="searchModel.name" placeholder="请输入用户名" />
      </el-form-item>
      <el-form-item label="电话:" prop="phone">
        <el-input v-model="searchModel.phone" placeholder="请输入电话" />
      </el-form-item>
    </el-form>
  </div>
  <!-- 按钮区 -->
  <div class="action">
    <el-button
      color="#626aef"
      :icon="CirclePlusFilled"
      @click="addCoachClick"
      plain
      >新增</el-button
    >
    <el-button color="#626aef" :icon="Edit" @click="editCoachClick" plain
      >修改</el-button
    >
    <el-button color="#626aef" :icon="Search" @click="search" plain
      >查询</el-button
    >
    <el-button color="#626aef" :icon="Refresh" @click="resetFrom" plain
      >重置</el-button
    >
    <el-button type="danger" :icon="Delete" @click="deleteCoachClick" plain
      >删除</el-button
    >
  </div>
  <!-- 数据显示区 -->
  <div class="data-grid">
    <!-- 数据表格 -->
    <el-table
      :data="coachs"
      :stripe="true"
      :border="true"
      :highlight-current-row="true"
      @row-click="tableRowClick"
      class="custom-table"
      v-loading="loading"
      ref="tableRef"
      style="width: 100%"
    >
      <!-- 列设置 -->
      <el-table-column type="selection" width="60" />
      <el-table-column prop="id" label="主键" width="60" />
      <el-table-column prop="name" label="姓名" width="140" />
      <el-table-column prop="phone" label="手机号" width="140" />
      <el-table-column prop="wechat" label="微信" width="140" />
      <el-table-column
        prop="recomm"
        label="导师寄语"
        width="270"
        :show-overflow-tooltip="true"
        :tooltip-formatter="recommFunc"
      />
      <el-table-column prop="photo" label="导师图片" width="100">
        <template #default="scope">
          <el-image
            :key="'/static' + scope.row.photo"
            :src="'/static' + scope.row.photo"
            lazy
            class="elimagesize"
          />
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

  <!-- 对话框 -->
  <el-dialog
    v-model="dialogFormVisible"
    :draggable="true"
    :title="dialogTitle"
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
          placeholder="请输入姓名"
          autocomplete="off"
        />
      </el-form-item>
      <el-form-item label="phone" prop="phone">
        <el-input
          v-model="formModel.phone"
          placeholder="请输入电话"
          autocomplete="off"
        />
      </el-form-item>
      <el-form-item label="wechat" prop="wechat">
        <el-input
          v-model="formModel.wechat"
          placeholder="请输入微信"
          autocomplete="off"
        />
      </el-form-item>
      <el-form-item label="recomm" prop="recomm">
        <el-input
          v-model="formModel.recomm"
          placeholder="请输入寄语"
          autocomplete="off"
        />
      </el-form-item>
      <el-form-item label="photo" prop="photo">
        <el-upload
          class="avatar-uploader"
          action="/static/image/upload/coach"
          name="file"
          method="post"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload"
        >
          <img v-if="imgAvatarUrl" :src="imgAvatarUrl" class="avatar" />
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="doCancel">Cancel</el-button>
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
.elimagesize {
  width: 50px;
  height: 50px;
}
.avatar-uploader .avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>
<style>
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}
</style>
<script setup>
import { ref, reactive, onMounted, toRaw } from "vue";
import {
  Search,
  Edit,
  Delete,
  CirclePlusFilled,
  Refresh,
  Plus,
} from "@element-plus/icons-vue";
import { findAll, deleteCoachRequest, save, update } from "@/api/CoachApi";
import { deleteImg } from "@/api/DeleteApi";
import { ElMessage, ElMessageBox } from "element-plus";
import lodash from "lodash";

let imgAvatarUrl = ref("");

// 图片上传
function beforeAvatarUpload(rawFile) {
  if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error("图片最大为2MB!");
    return false;
  }
  return true;
}

function handleAvatarSuccess(resp) {
  imgAvatarUrl.value = "/static" + resp.data;
}
// 导师寄语
function recommFunc() {}

const loading = ref(true);
//搜索表单数据模型
const searchModel = reactive({
  id: "",
  name: "",
  phone: "",
});
// 搜索表单实例
const searchFromRef = ref();
// 重置按钮事件
function resetFrom() {
  searchFromRef.value.resetFields();
}

//分页数据
let pi = ref({
  page: 1,
  limit: 10,
  total: 0,
});
//所有用户数据
const coachs = ref([]);
//查询全部用户
async function search() {
  loading.value = true;
  //转换成原始格式
  let params = toRaw(searchModel);
  let resp = await findAll(pi.value.page, pi.value.limit, params);
  if (resp.success && resp.data.length > 0) {
    coachs.value = resp.data;
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
function deleteCoachClick() {
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
      deleteCoachs(ids);
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
async function deleteCoachs(ids) {
  let resp = await deleteCoachRequest(ids);
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

// 对话框关闭的时候调用此函数清空表单
let auformRef = ref();
function diaglogClose() {
  auformRef.value.resetFields();
  Object.assign(formModel, {
    id: "",
    name: "",
    phone: "",
    wechat: "",
    recomm: "",
    photo: "",
  });
  // NOTE:删除照片
  // imgAvatarUrl.value = imgAvatarUrl.value.replace("/static", "");
  // deleteImg(imgAvatarUrl.value);
  // imgAvatarUrl.value = "";
}

// 对话框设置
let dialogFormVisible = ref(false);
let dialogTitle = ref();
let formModel = reactive({
  id: "",
  name: "",
  phone: "",
  wechat: "",
  recomm: "",
  photo: "",
});
// 添显示加对话框
function addCoachClick() {
  dialogFormVisible.value = true;
  dialogTitle.value = "Add Coach";
}

// 修改对话框
function editCoachClick() {
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
    // 不可以直接把cloneFromModel包装成响应式赋值，这样会创建新的响应式对象，原来绑定的就会失效
    Object.assign(formModel, lodash.cloneDeep(rows[0]));
    // NOTE:1.这是第一步
    imgAvatarUrl.value = "/static" + formModel.photo;
    dialogFormVisible.value = true;
    dialogTitle.value = "Edit Coach";
  }
}
// 表单验证规则
const rules = {
  name: [
    { required: true, message: "请输入姓名", trigger: "blur" },
    { min: 2, max: 10, message: "姓名的长度应该在2-10之间", trigger: "blur" },
  ],
  phone: [
    { required: true, message: "请输入手机号", trigger: "blur" },
    { min: 11, max: 11, message: "手机号必须为11位", trigger: "blur" },
  ],
  password: [
    { required: true, message: "请输入密码", submitLogintrigger: "blur" },
    { min: 6, max: 20, message: "密码长度应该在6-20之间", trigger: "blur" },
  ],
};
// 事件
async function doSubmit() {
  auformRef.value.validate(async (valid) => {
    if (valid) {
      // NOTE:头像修改
      formModel.photo = imgAvatarUrl.value.replace("/static", "");
      let coach = toRaw(formModel);
      if (coach.id && coach.id !== "") {
        let resp = await update(coach);
        if (resp.success) {
          // NOTE:如果照片更新成功应该删除原来的照片
          imgAvatarUrl.value = '';
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
        let resp = await save(coach);
        if (resp.success) {
          ElMessage({
            type: "success",
            message: resp.message,
          });
          search();
          dialogFormVisible.value = false;
        } else {
          // 实际上到不了这个地方，因为失败返回的是一个badRequest
          ElMessage({
            type: "error",
            message: resp.message,
          });
        }
      }
    } else {
      ElMessage({
        type: "error",
        message: "请检查您的数据后再尝试",
      });
    }
  });
}
// 取消事件
function doCancel() {
  dialogFormVisible.value = false;
  ElMessage({
    type: "info",
    message: dialogTitle.value + " Cancel",
  });
}

//当页面就绪之后
onMounted(() => {
  search(); //异步函数
});
</script>