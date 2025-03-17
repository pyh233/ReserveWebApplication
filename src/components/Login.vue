 <template>
  <div class="login-container">
    <el-card class="login-card">
      <h2 class="login-title">登录</h2>
      <el-form
        :model="loginFormData"
        ref="loginFormRef"
        :rules="rules"
        label-width="80px"
      >
        <el-form-item label="账号" prop="phone">
          <el-input v-model="loginFormData.phone" placeholder="请输入账号" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input
            v-model="loginFormData.password"
            type="password"
            placeholder="请输入密码"
            show-password
          />
        </el-form-item>
        <el-form-item label="验证码" prop="captcha">
          <el-input
            v-model="loginFormData.captcha"
            placeholder="请输入验证码"
          />
          <img :src="captchaUrl" alt="验证码" @click="refreshCaptcha" />
        </el-form-item>
        <el-form-item>
          <el-checkbox v-model="loginFormData.rememberMe">记住密码</el-checkbox>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="login">登录</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref, toRaw } from "vue";
import { ElMessage } from "element-plus";
import { getCaptcha, adminLogin } from "@/api/AdminLoginApi";
import {save,get,remove} from "@/util/token";
import { useRouter } from "vue-router";

// 路由派发器
const router = useRouter();

// 验证码地址
const captchaUrl = ref();
// 发送验证码请求验证码图片
async function refreshCaptcha() {
  // 获取二进制验证码图片数据，并且设置图片
  let resp = await getCaptcha();
  let blobCaptchaData = resp.data;
  let url = URL.createObjectURL(blobCaptchaData);
  captchaUrl.value = url; 

  // 获取验证码标识
  let captchaCode = resp.headers["captcha-code"];
  loginFormData.captchaCode = captchaCode;
}
// 发送登录请求
async function login() {
  loginFormRef.value.validate(async (valid) => {
    if (valid) {
      let resp = await adminLogin(toRaw(loginFormData));
      if (resp.data.success) {
        save(resp.data.data.jwt);
        router.push("/");
        // NOTE:登陆成功保存token并且跳转
      } else {
        ElMessage.error(resp.data.message);
      }
    } else {
      ElMessage.error("请填写完整信息");
    }
  });
}
// 表单实例
const loginFormRef = ref();
//表单信息
const loginFormData = reactive({
  phone: "",
  password: "",
  captcha: "",
  captchaCode: "",
  rememberMe: false,
});
// 验证规则
const rules = {
  phone: [{ required: true, message: "请输入用户名", trigger: "blur" }],
  password: [{ required: true, message: "请输入密码", trigger: "blur" }],
  captcha: [{ required: true, message: "请输入验证码", trigger: "blur" }],
};
// 重置按钮
function resetForm() {
  loginFormRef.value.resetFields();
}
onMounted(() => {
  refreshCaptcha();
});
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f5f5;
}

.login-card {
  width: 350px;
  padding: 20px;
  text-align: center;
}

.login-title {
  margin-bottom: 20px;
  font-size: 22px;
  color: #333;
}
</style>
