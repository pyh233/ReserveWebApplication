package com.example.psychroomserver.api;

import com.example.psychroomserver.DTO.PermissionRouteDTO;
import com.example.psychroomserver.DTO.RolePermissionDTO;
import com.example.psychroomserver.model.chown.Permission;
import com.example.psychroomserver.model.chown.Role;
import com.example.psychroomserver.model.search.PermissionSearchModel;
import com.example.psychroomserver.model.search.RoleSearchModel;
import com.example.psychroomserver.service.PermissionService;
import com.example.psychroomserver.service.RoleService;
import com.example.psychroomserver.util.JsonResult;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/role")
public class RoleApi {
    RoleService roleService;
    PermissionService permissionService;

    @Autowired
    public void setPermissionService(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<JsonResult> getRoles(@RequestParam(defaultValue = "1") Integer page,
                                                    @RequestParam(defaultValue = "10") Integer limit,
                                                    RoleSearchModel roleSearchModel) {
        Page<?> p = new Page<>(page, limit);
        List<Role> roleList = roleService.findAllRoles(roleSearchModel, p);
        PageInfo pi = new PageInfo(roleList);
        if (!roleList.isEmpty()) {
            JsonResult jr = JsonResult.success("查询成功", roleList);
            jr.getPageInfo().setTotal(pi.getTotal());
            jr.getPageInfo().setPageNum(p.getPageNum());
            jr.getPageInfo().setPageSize(p.getPageSize());
            return ResponseEntity.ok(jr);
        } else {
            return ResponseEntity.ok(JsonResult.fail("查询失败"));
        }
    }

    @DeleteMapping
    public ResponseEntity<JsonResult> deleteRoles(@RequestBody Integer[] ids) {

        int count = roleService.deleteRoleByIds(ids);
        if (count > 0) {
            return ResponseEntity.ok(JsonResult.success("成功删除" + count + "条数据", null));
        } else {
            return ResponseEntity.ok(JsonResult.fail("删除失败"));
        }
    }

    @PostMapping
    public ResponseEntity<JsonResult> addRole(@RequestBody Role role) {
        boolean success = roleService.addRole(role);
        if (success) {
            return ResponseEntity.ok(JsonResult.success("添加成功", null));
        } else {
            return ResponseEntity.ok(JsonResult.fail("删除失败"));
        }
    }

    /**
     * 列表使用
     * @return
     */
    @GetMapping("/permission")
    public ResponseEntity<JsonResult> getPermission() {
        return ResponseEntity.ok(JsonResult.success("查询成功", permissionService.getAllPermissions()));
    }

    @PutMapping
    public ResponseEntity<JsonResult> updateRole(@RequestBody RolePermissionDTO rp) {
        //1.先删除数据库中间表中跟当前权限有关的所有数据
        //2.再添加传来的所有id
        roleService.renewPermission4Role(rp.getRole().getId(),rp.getSelectedPermissions());
        return ResponseEntity.ok(JsonResult.success("成功改变权限", null));
    }
}
