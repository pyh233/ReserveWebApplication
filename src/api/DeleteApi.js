import axiosInstance from "@/util/deleteStatic"

const deleteImgUrl = "/image/delete";

function deleteImg(path) {
    let resp = axiosInstance({
        url:deleteImgUrl,
        method: "delete",
        data: path
    });
    return resp;
};
export {deleteImg};