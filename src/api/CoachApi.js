import axiosInstance from "@/util/api"

const url = "/coach";
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
function deleteCoachRequest(ids) {
    let resp = axiosInstance({
        url,
        method: "delete",
        data: ids
    });
    return resp;
};
function save(coach) {
    let resp = axiosInstance({
        url,
        method: "post",
        data:coach
    });
    return resp;
}
function update(coach) {
    let resp = axiosInstance({
        url,
        method: "put",
        data:coach
    });
    return resp;
}
export { findAll, deleteCoachRequest, save,update }