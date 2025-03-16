package com.example.psychroomserver.api;

import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.psychroomserver.DTO.AdminGroupDTO;
import com.example.psychroomserver.model.Admin;
import com.example.psychroomserver.model.chown.Group;
import com.example.psychroomserver.model.search.AdminSearchModel;
import com.example.psychroomserver.model.search.GroupSearchModel;
import com.example.psychroomserver.service.AdminService;
import com.example.psychroomserver.service.GroupService;
import com.example.psychroomserver.util.JsonResult;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Delete;
import org.jasypt.util.password.PasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/admin", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminApi {
    private AdminService adminService;
    private GroupService groupService;
    /*密钥，在配置文件中配置。存储在服务端，保密，可定期更换*/
//    @Value("${jwt.secret.key}")
//    private String secretKey;

    @Autowired
    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }

    @Autowired
    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/login")
    public ResponseEntity<JsonResult> login(@RequestBody Admin loginAdmin) {
        // 数据验证
        if (!StringUtils.hasText(loginAdmin.getPhone())) {
            throw new IllegalArgumentException("用户名不可为空");
        }
        return adminService.loginAdmin(loginAdmin);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<JsonResult> logout(@RequestBody String token) {
        // NOTE:这里不需要传递token，stp接收到的cookie就能帮我们登出
        StpUtil.logout();
        return ResponseEntity.ok(JsonResult.success("登出成功，即将跳转到登陆页面", null));
    }


    @GetMapping
    public ResponseEntity<JsonResult> findAllAdmin(@RequestParam(defaultValue = "1") Integer page,
                                                   @RequestParam(defaultValue = "10") Integer limit,
                                                   AdminSearchModel adminSearchModel) {
        Page<?> p = new Page<>(page, limit);
        List<Admin> adminList = adminService.findAllAdmins(adminSearchModel, p);
        PageInfo pi = new PageInfo(adminList);
        if (!adminList.isEmpty()) {
            JsonResult jr = JsonResult.success("查询成功", adminList);
            jr.getPageInfo().setTotal(pi.getTotal());
            jr.getPageInfo().setPageNum(p.getPageNum());
            jr.getPageInfo().setPageSize(p.getPageSize());
            return ResponseEntity.ok(jr);
        } else {
            return ResponseEntity.ok(JsonResult.fail("查询失败"));
        }
    }

    @DeleteMapping
    public ResponseEntity<JsonResult> deleteAdmins(@RequestBody Integer[] ids) {
        int count = adminService.deleteAdminByIds(ids);
        if (count > 0) {
            return ResponseEntity.ok(JsonResult.success("成功删除" + count + "条数据", null));
        } else {
            return ResponseEntity.ok(JsonResult.fail("删除失败"));
        }
    }

    @PostMapping
    public ResponseEntity<JsonResult> addAdmin(@RequestBody Admin admin) {
        boolean success = adminService.addAdmin(admin);
        if (success) {
            return ResponseEntity.ok(JsonResult.success("添加成功", null));
        } else {
            return ResponseEntity.ok(JsonResult.fail("添加失败"));
        }
    }

    @PutMapping
    public ResponseEntity<JsonResult> updateAdmin(@RequestBody Admin admin) {
        System.out.println(admin);
        boolean success = adminService.updateAdmin(admin);
        if (success) {
            return ResponseEntity.ok(JsonResult.success("修改成功", null));
        } else {
            return ResponseEntity.ok(JsonResult.fail("修改失败"));
        }
    }

    /**
     * 列表使用
     *
     * @return
     */
    @GetMapping("/group")
    public ResponseEntity<JsonResult> getGroup() {
        return ResponseEntity.ok(JsonResult.success("sc", groupService.getAllGroups()));
    }

    @PutMapping("/group")
    public ResponseEntity<JsonResult> updateGroup(@RequestBody AdminGroupDTO ag) {
        adminService.renewGroup4Admin(ag.getAdmin().getId(), ag.getSelectedGroups());
        return ResponseEntity.ok(JsonResult.success("更新成功", null));
    }
}
