import axiosInstance from "@/util/api"

const url = "/user";
function findAll(page = 1, limit = 15, params = {}) {
    let resp = axiosInstance({
        url,
        method: "get",
        params: {
            page,
            limit,
            ...params
        }
    });
    console.log(resp);
    return resp;
};
function deleteUserRequest(ids) {
    let resp = axiosInstance({
        url,
        method: "delete",
        data: ids
    });
    return resp;
};
function save(user) {
    let resp = axiosInstance({
        url,
        method: "post",
        data:user
    });
    return resp;
}
function update(user) {
    let resp = axiosInstance({
        url,
        method: "put",
        data:user
    });
    return resp;
}
export { findAll, deleteUserRequest, save,update }