package com.example.psychroomserver.model;


public enum ReserveStatus {
    CREATED("已预定"),RECEIVER("接受预定"),
    CONTINUE("持续中"),FINISH("正常结束"),
    APPLY_FIN("申请结束"),FINISH_AHEAD("提前结束");

    private String status;
    ReserveStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return this.status;
    }
}
