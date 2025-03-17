import axiosInstance from "@/util/api"

const url = "/role";
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
function deleteRoleRequest(ids) {
    let resp = axiosInstance({
        url,
        method: "delete",
        data: ids
    });
    return resp;
};
function save(role) {
    let resp = axiosInstance({
        url,
        method: "post",
        data: role
    });
    return resp;
}
function update(role, selectedPermissions) {
    let resp = axiosInstance({
        url,
        method: "put",
        data: {
            role,
            selectedPermissions
        }
    });
    return resp;
}
const getAllPermissionUrl = "/role/permission"
function getAllPermission() {
    let resp = axiosInstance({
        url: getAllPermissionUrl,
        method: "get"
    });
    return resp;
}
export { findAll, deleteRoleRequest, save, update, getAllPermission }