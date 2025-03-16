package com.example.psychroomserver.handler;

import cn.dev33.satoken.exception.NotLoginException;
import com.example.psychroomserver.util.JsonResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // 用户未登录异常
    @ExceptionHandler(NotLoginException.class)
    public ResponseEntity<JsonResult> notLogin(NotLoginException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(JsonResult.fail("认证失败，令牌过期或者无效，即将前往登录页面"));
    }
    // 数据库约束限制添加权限异常
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<JsonResult> sql(SQLException e) {
        return ResponseEntity.internalServerError().body(JsonResult.fail("请检查您的数据后重新提交"));
    }
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<JsonResult> exception(Exception e) {
//        return ResponseEntity.internalServerError().body(JsonResult.fail("发生了未知错误"));
//    }
}
