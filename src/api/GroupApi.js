import axiosInstance from "@/util/api"

const url = "/group";
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
function deleteGroupRequest(ids) {
    let resp = axiosInstance({
        url,
        method: "delete",
        data: ids
    });
    return resp;
};
function save(group) {
    let resp = axiosInstance({
        url,
        method: "post",
        data: group
    });
    return resp;
}
function update(group, selectedRoles) {
    let resp = axiosInstance({
        url,
        method: "put",
        data: {
            group,
            selectedRoles
        }
    });
    return resp;
}
const getAllRoleUrl = "/group/role"
function getAllRole() {
    let resp = axiosInstance({
        url: getAllRoleUrl,
        method: "get"
    });
    return resp;
}
export { findAll, deleteGroupRequest, save, update, getAllRole }