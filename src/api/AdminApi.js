import axiosInstance from "@/util/api"

const url = "/admin";
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
    return resp;
};
function deleteAdminRequest(ids) {
    let resp = axiosInstance({
        url,
        method: "delete",
        data: ids
    });
    return resp;
};
function save(admin) {
    let resp = axiosInstance({
        url,
        method: "post",
        data: admin
    });
    return resp;
}
function updateAdmin(admin) {
    let resp = axiosInstance({
        url,
        method: "put",
        data: admin
    });
    return resp;
}
function update(admin, selectedGroups) {
    let resp = axiosInstance({
        url: url + "/group",
        method: "put",
        data: {
            admin,
            selectedGroups
        }
    });
    return resp;
}
const getAllGroupUrl = "/admin/group"
function getAllGroup() {
    let resp = axiosInstance({
        url: getAllGroupUrl,
        method: "get"
    });
    return resp;
}
export { findAll, deleteAdminRequest, updateAdmin, save, update, getAllGroup }