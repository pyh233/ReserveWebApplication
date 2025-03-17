import axiosInstance from "@/util/api"

const url = "/room";
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
function deleteRoomRequest(ids) {
    let resp = axiosInstance({
        url,
        method: "delete",
        data: ids
    });
    return resp;
};
function save(room) {
    let resp = axiosInstance({
        url,
        method: "post",
        data:room
    });
    return resp;
}
function update(room) {
    let resp = axiosInstance({
        url,
        method: "put",
        data:room
    });
    return resp;
}
export { findAll, deleteRoomRequest, save,update }