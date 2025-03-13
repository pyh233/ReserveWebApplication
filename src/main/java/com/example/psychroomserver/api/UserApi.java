package com.example.psychroomserver.api;

import cn.dev33.satoken.stp.StpUtil;
import com.example.psychroomserver.model.Admin;
import com.example.psychroomserver.model.User;
import com.example.psychroomserver.model.search.UserSearchModel;
import com.example.psychroomserver.service.UserService;
import com.example.psychroomserver.util.JsonResult;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserApi {
    UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<JsonResult> getUser(@RequestParam(defaultValue = "1") Integer page,
                                              @RequestParam(defaultValue = "10") Integer limit,
                                              UserSearchModel userSearchModel) {
        Page<?> p = new Page<>(page, limit);
        List<User> userList = userService.getAllUsers(userSearchModel, p);
        PageInfo pi = new PageInfo(userList);
        if (!userList.isEmpty()) {
            JsonResult jr = JsonResult.success("查询成功", userList);
            jr.getPageInfo().setTotal(pi.getTotal());
            jr.getPageInfo().setPageNum(p.getPageNum());
            jr.getPageInfo().setPageSize(p.getPageSize());
            return ResponseEntity.ok(jr);
        } else {
            return ResponseEntity.ok(JsonResult.fail("查询失败"));
        }
    }

    @DeleteMapping
    public ResponseEntity<JsonResult> deleteUser(@RequestBody Integer[] ids) {

        int count = userService.deleteUserByIds(ids);
        if (count > 0) {
            return ResponseEntity.ok(JsonResult.success("成功删除" + count + "条数据", null));
        } else {
            return ResponseEntity.ok(JsonResult.fail("删除失败"));
        }
    }

    @PostMapping
    public ResponseEntity<JsonResult> addUser(@RequestBody User user) {
        boolean success = userService.addUser(user);
        if (success) {
            return ResponseEntity.ok(JsonResult.success("添加成功", null));
        } else {
            return ResponseEntity.ok(JsonResult.fail("删除失败"));
        }
    }

    @PutMapping
    public ResponseEntity<JsonResult> updateUser(@RequestBody User user) {
        boolean success = userService.updateUser(user);
        if (success) {
            return ResponseEntity.ok(JsonResult.success("修改成功", null));
        } else {
            return ResponseEntity.ok(JsonResult.fail("修改失败"));
        }
    }
}
