package com.example.psychroomserver.util;

import com.github.pagehelper.PageInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class JsonResult {
    int code;
    boolean success;
    String message;
    Object data;
    PageInfo<?> pageInfo;
    int totalCount;

    public JsonResult(int code, boolean success, String message, Object data) {
        this.pageInfo = new PageInfo<>();
        this.code = code;
        this.success = success;
        this.message = message;
        this.data = data;
    }
    public static JsonResult success(String message, Object data) {
        return new JsonResult(200, true, message, data);
    }
    public static JsonResult fail(String message) {
        return new JsonResult(500, false, message, null);
    }
}
