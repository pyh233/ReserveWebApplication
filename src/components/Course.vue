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
        <el-input v-model="searchModel.name" placeholder="请输入课程名称" />
      </el-form-item>
    </el-form>
  </div>
  <!-- 按钮区 -->
  <div class="action">
    <el-button
      color="#626aef"
      :icon="CirclePlusFilled"
      @click="addCourseClick"
      plain
      >新增</el-button
    >
    <el-button color="#626aef" :icon="Edit" @click="editCourseClick" plain
      >修改</el-button
    >
    <el-button color="#626aef" :icon="Search" @click="search" plain
      >查询</el-button
    >
    <el-button color="#626aef" :icon="Refresh" @click="resetFrom" plain
      >重置</el-button
    >
    <el-button type="danger" :icon="Delete" @click="deleteCourseClick" plain
      >删除</el-button
    >
  </div>
  <!-- 数据显示区 -->
  <div class="data-grid">
    <!-- 数据表格 -->
    <el-table
      :data="courses"
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
      <el-table-column prop="name" label="课程名称" width="140" />
      <el-table-column prop="time" label="开始时间" width="170" />
      <el-table-column prop="length" label="课时" width="140" />
      <el-table-column prop="photo" label="照片"/>
      <el-table-column prop="coachId" label="教练" />
      <el-table-column prop="roomId" label="房间" />
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
          placeholder="请输入课程名"
          autocomplete="off"
        />
      </el-form-item>
      <el-form-item label="time" prop="time">
        <el-date-picker
        v-model="formModel.time"
        type="datetime"
        placeholder="请选择时间"
      />
      </el-form-item>
      <el-form-item label="length" prop="length">
        <el-input
          v-model="formModel.length"
          placeholder="请输入课时"
          autocomplete="off"
        />
      </el-form-item>
      <el-form-item label="photo" prop="photo">
        <el-input
          v-model="formModel.photo"
          placeholder="请输入照片"
          autocomplete="off"
        />
      </el-form-item>
      <el-form-item label="coachId" prop="coachId">
        <el-input
          v-model="formModel.coachId"
          placeholder="请输入jl"
          autocomplete="off"
        />
      </el-form-item>
      <el-form-item label="roomId" prop="roomId">
        <el-input
          v-model="formModel.roomId"
          placeholder="请输入fj"
          autocomplete="off"
        />
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
</style>

<script setup>
import { ref, reactive, onMounted, toRaw } from "vue";
import {
  Search,
  Edit,
  Delete,
  CirclePlusFilled,
  Refresh,
} from "@element-plus/icons-vue";
import { findAll, deleteCourseRequest, save, update } from "@/api/CourseApi";
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
const courses = ref([]);
//查询全部用户
async function search() {
  loading.value = true;
  //转换成原始格式
  let params = toRaw(searchModel);
  let resp = await findAll(pi.value.page, pi.value.limit, params);
  if (resp.success && resp.data.length > 0) {
    courses.value = resp.data;
    pi.value = {
      total: resp.pageInfo.total,
      page: resp.pageInfo.pageNum,
      limit: resp.pageInfo.pageSize,
    };
  }
  loading.value = false;
}

//搜索表单数据模型
const searchModel = reactive({
  id: "",
  name: "",
});
// 搜索表单实例
const searchFromRef = ref();
// 重置按钮事件
function resetFrom() {
  searchFromRef.value.resetFields();
}

// 表格实例
let tableRef = ref();
// 表格行点击事件
function tableRowClick(row) {
  tableRef.value.toggleRowSelection(row);
}
// 点击删除
// TODO:存在一个删除全页面的情况
function deleteCourseClick() {
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
      deleteCourses(ids);
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
async function deleteCourses(ids) {
  let resp = await deleteCourseRequest(ids);
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
    password: "",
    isDel: true,
  });
}

// 对话框设置
let dialogFormVisible = ref(false);
let dialogTitle = ref();
let formModel = reactive({
  id: "",
  name: "",
  time: "",
  length: "",
  photo:"",

  coachId:"",
  roomId:""
});
// 添显示加对话框
function addCourseClick() {
  dialogFormVisible.value = true;
  dialogTitle.value = "Add Course";
}

// 修改对话框
function editCourseClick() {
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
    let cloneFromModel = lodash.cloneDeep(rows[0]);
    Object.assign(formModel, cloneFromModel);
    dialogFormVisible.value = true;
    dialogTitle.value = "Edit Course";
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
    { required: true, message: "请输入密码", trigger: "blur" },
    { min: 6, max: 20, message: "密码长度应该在6-20之间", trigger: "blur" },
  ],
};
// 事件
async function doSubmit() {
  auformRef.value.validate(async (valid) => {
    if (valid) {
      let course = toRaw(formModel);
      if (course.id && course.id !== "") {
        let resp = await update(course);
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
        let resp = await save(course);
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