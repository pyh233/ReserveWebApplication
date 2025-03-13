package com.example.psychroomserver.api;

import com.example.psychroomserver.DTO.PermissionRouteDTO;
import com.example.psychroomserver.model.chown.Permission;
import com.example.psychroomserver.model.chown.Route;
import com.example.psychroomserver.model.search.PermissionSearchModel;
import com.example.psychroomserver.service.PermissionService;
import com.example.psychroomserver.service.RouteService;
import com.example.psychroomserver.util.JsonResult;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/permission")
public class PermissionApi {
    PermissionService permissionService;
    RouteService routeService;

    @Autowired
    public void setRouteService(RouteService routeService) {
        this.routeService = routeService;
    }

    @Autowired
    public void setPermissionService(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping
    public ResponseEntity<JsonResult> getPermission(@RequestParam(defaultValue = "1") Integer page,
                                                    @RequestParam(defaultValue = "10") Integer limit,
                                                    PermissionSearchModel permissionSearchModel) {
        Page<?> p = new Page<>(page, limit);
        List<Permission> permissionList = permissionService.getPermissions(permissionSearchModel, p);
        PageInfo pi = new PageInfo(permissionList);
        if (!permissionList.isEmpty()) {
            JsonResult jr = JsonResult.success("查询成功", permissionList);
            jr.getPageInfo().setTotal(pi.getTotal());
            jr.getPageInfo().setPageNum(p.getPageNum());
            jr.getPageInfo().setPageSize(p.getPageSize());
            return ResponseEntity.ok(jr);
        } else {
            return ResponseEntity.ok(JsonResult.fail("查询失败"));
        }
    }

    @DeleteMapping
    public ResponseEntity<JsonResult> deletePermission(@RequestBody Integer[] ids) {

        int count = permissionService.deletePermissionByIds(ids);
        if (count > 0) {
            return ResponseEntity.ok(JsonResult.success("成功删除" + count + "条数据", null));
        } else {
            return ResponseEntity.ok(JsonResult.fail("删除失败"));
        }
    }

    @PostMapping
    public ResponseEntity<JsonResult> addPermission(@RequestBody Permission permission) {
        boolean success = permissionService.addPermission(permission);
        if (success) {
            return ResponseEntity.ok(JsonResult.success("添加成功", null));
        } else {
            return ResponseEntity.ok(JsonResult.fail("删除失败"));
        }
    }

    @GetMapping("/route")
    public ResponseEntity<JsonResult> getRoute() {
        return ResponseEntity.ok(JsonResult.success("查询成功",routeService.getAllRoutes()));
    }

    @PutMapping
    public ResponseEntity<JsonResult> updatePermission(@RequestBody PermissionRouteDTO pr) {
        //1.先删除数据库中间表中跟当前权限有关的所有数据
        //2.再添加传来的所有id
        System.out.println(pr.getPermission());
        System.out.println(Arrays.toString(pr.getSelectedRoutes()));
        return null;
    }
}
