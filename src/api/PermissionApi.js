import axiosInstance from "@/util/api"

const url = "/permission";
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
function deletePermissionRequest(ids) {
    let resp = axiosInstance({
        url,
        method: "delete",
        data: ids
    });
    return resp;
};
function save(permission) {
    let resp = axiosInstance({
        url,
        method: "post",
        data: permission
    });
    return resp;
}
function update(permission, selectedRoutes) {
    let resp = axiosInstance({
        url,
        method: "put",
        data: {
            permission,
            selectedRoutes
        }
    });
    return resp;
}
const getAllRouteUrl = "/permission/route"
function getAllRoute() {
    let resp = axiosInstance({
        url: getAllRouteUrl,
        method: "get"
    });
    return resp;
}
export { findAll, deletePermissionRequest, save, update, getAllRoute }