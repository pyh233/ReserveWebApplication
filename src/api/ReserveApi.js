import axiosInstance from "@/util/api"

const url = "/reserve";
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
function deleteReserveRequest(ids) {
    let resp = axiosInstance({
        url,
        method: "delete",
        data: ids
    });
    return resp;
};
function save(reserve) {
    let resp = axiosInstance({
        url,
        method: "post",
        data:reserve
    });
    return resp;
}
function update(reserve) {
    let resp = axiosInstance({
        url,
        method: "put",
        data:reserve
    });
    return resp;
}
export { findAll, deleteReserveRequest, save,update }