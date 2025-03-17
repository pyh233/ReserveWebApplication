import axiosInstance from "@/util/LoginApi"

const captchaUrl = "/common/captcha";
function getCaptcha() {
    let resp = axiosInstance({
        url: captchaUrl,
        method: "get",
        responseType: "blob"
    });
    // 需要请求头 返回全部response
    return resp;
}
const loginUrl = "/admin/login";
async function adminLogin(data) {
    let resp = await axiosInstance({
        url: loginUrl,
        method: "post",
        data: data
    });
    return resp;
}
const logoutUrl = "/admin/logout";
async function adminLogout(token) {
    let resp = await axiosInstance({
        url: logoutUrl,
        method: "delete",
        data:  token
    });
    return resp;
}
export { getCaptcha, adminLogin,adminLogout };