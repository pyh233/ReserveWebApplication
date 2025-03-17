// 创建一个axios实例
import { get } from "@/util/token";
import axios from "axios";
import { ElMessage } from "element-plus";

const axiosInstance = axios.create({
    baseURL: "/api",
    timeout: 3000
});
// 添加请求拦截器
axiosInstance.interceptors.request.use(config => {
    config.headers["x-Request-With"] = "axios";
    // config.headers["Authorization"] = get();
    // NOTE:数据请求接口 只有登陆以后才能发送请求，否则跳转到登录
    if (get()) {
        // WHY:貌似不需要sa-token，satoken使用cookie?
        config.headers["sa-token"] = get();
        return config;
    } else {
        location.href = "/login";
        return Promise.reject(config);
    }
}, error => {
    return Promise.reject(error);
});


// 添加响应拦截器
axiosInstance.interceptors.response.use(resp => {
    return resp.data;
}, error => {
    if (error.status === 403) {
        // 后端权限认证不通过
        ElMessage.error("没有访问权限,请联系管理员!");
    } else if (error.status === 401) {// 未登录错误
        ElMessage.error(error.response.data.message);
        setTimeout(() => {
            location.href = "/login";
        }, 3000);
    } else {//其他错误 //NOTE:可以不报让组件去处理
        ElMessage.error(error.response.data.message);
    }
    return Promise.reject(error.response.data);
});

export default axiosInstance;