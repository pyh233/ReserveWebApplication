package com.example.psychroomserver.api;

import com.example.psychroomserver.model.chown.Route;
import com.example.psychroomserver.model.search.RouteSearchModel;
import com.example.psychroomserver.service.RouteService;
import com.example.psychroomserver.util.JsonResult;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/route")
public class RouteApi {
    RouteService routeService;

    @Autowired
    public void setRouteService(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping
    public ResponseEntity<JsonResult> getRoute(@RequestParam(defaultValue = "1") Integer page,
                                              @RequestParam(defaultValue = "10") Integer limit,
                                              RouteSearchModel routeSearchModel) {
        Page<?> p = new Page<>(page, limit);
        List<Route> routeList = routeService.findAllRoutes(routeSearchModel, p);
        PageInfo pi = new PageInfo(routeList);
        if (!routeList.isEmpty()) {
            JsonResult jr = JsonResult.success("查询成功", routeList);
            jr.getPageInfo().setTotal(pi.getTotal());
            jr.getPageInfo().setPageNum(p.getPageNum());
            jr.getPageInfo().setPageSize(p.getPageSize());
            return ResponseEntity.ok(jr);
        } else {
            return ResponseEntity.ok(JsonResult.fail("查询失败"));
        }
    }

    @DeleteMapping
    public ResponseEntity<JsonResult> deleteRoute(@RequestBody Integer[] ids) {

        int count = routeService.deleteRouteByIds(ids);
        if (count > 0) {
            return ResponseEntity.ok(JsonResult.success("成功删除" + count + "条数据", null));
        } else {
            return ResponseEntity.ok(JsonResult.fail("删除失败"));
        }
    }

    @PostMapping
    public ResponseEntity<JsonResult> addRoute(@RequestBody Route route) {
        boolean success = routeService.addRoute(route);
        if (success) {
            return ResponseEntity.ok(JsonResult.success("添加成功", null));
        } else {
            return ResponseEntity.ok(JsonResult.fail("删除失败"));
        }
    }

    @PutMapping
    public ResponseEntity<JsonResult> updateRoute(@RequestBody Route route) {
        boolean success = routeService.updateRoute(route);
        if (success) {
            return ResponseEntity.ok(JsonResult.success("修改成功", null));
        } else {
            return ResponseEntity.ok(JsonResult.fail("修改失败"));
        }
    }
}
