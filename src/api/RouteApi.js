import axiosInstance from "@/util/api"

const url = "/route";
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
function deleteRouteRequest(ids) {
    let resp = axiosInstance({
        url,
        method: "delete",
        data: ids
    });
    return resp;
};
function save(route) {
    let resp = axiosInstance({
        url,
        method: "post",
        data:route
    });
    return resp;
}
function update(route) {
    let resp = axiosInstance({
        url,
        method: "put",
        data:route
    });
    return resp;
}
export { findAll, deleteRouteRequest, save,update }