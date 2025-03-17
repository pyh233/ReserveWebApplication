// 创建一个axios实例
import axios from "axios";
import { ElMessage } from "element-plus";
const axiosInstance = axios.create({
    baseURL: "/api",
    timeout: 3000
});
// 添加请求拦截器
axiosInstance.interceptors.request.use(config => {
    config.headers["X-Request-With"] = "axios";
    return config;
},error => {
    return Promise.reject(error);
});
// 添加响应拦截器
axiosInstance.interceptors.response.use(resp => {
    // 登陆的时候需要请求头因此不能只返回resp.data
    return resp;
}, error => {//错误数据
    ElMessage.error(error.response.data.message);
    // ---------------------------------------
    return Promise.reject(error.response.data);
});

export default axiosInstance;