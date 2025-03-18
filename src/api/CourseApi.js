import axiosInstance from "@/util/api"

const url = "/course";
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
function deleteCourseRequest(ids) {
    let resp = axiosInstance({
        url,
        method: "delete",
        data: ids
    });
    return resp;
};
function save(course) {
    let resp = axiosInstance({
        url,
        method: "post",
        data: course
    });
    return resp;
}
function update(course) {
    let resp = axiosInstance({
        url,
        method: "put",
        data: course
    });
    return resp;
}
function getCoachsList() {
    let resp = axiosInstance({
        url: "/coach/list",
        method: "get"
    });
    return resp;
}
function getRoomsList() {
    let resp = axiosInstance({
        url: "/room/list",
        method: "get"
    });
    return resp;
}
export { findAll, deleteCourseRequest, save, update,getCoachsList,getRoomsList }