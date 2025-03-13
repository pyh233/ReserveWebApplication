package com.example.psychroomserver.api;

import com.example.psychroomserver.model.Reserve;
import com.example.psychroomserver.model.search.ReserveSearchModel;
import com.example.psychroomserver.service.ReserveService;
import com.example.psychroomserver.util.JsonResult;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reserve")
public class ReserveApi {
    ReserveService reserveService;

    @Autowired
    public void setReserveService(ReserveService reserveService) {
        this.reserveService = reserveService;
    }

    @GetMapping
    public ResponseEntity<JsonResult> getAllReserve(ReserveSearchModel reserveSearchModel,
                                                  @RequestParam(defaultValue = "1") Integer page,
                                                  @RequestParam(defaultValue = "10") Integer limit) {
        Page<?> p = new Page<>(page, limit);
        List<Reserve> reserveList = reserveService.getAllReserve(reserveSearchModel,p);
        PageInfo<Reserve> pi = new PageInfo(reserveList);
        if(!reserveList.isEmpty()){
            JsonResult jr = JsonResult.success("success", reserveList);
            jr.getPageInfo().setTotal(pi.getTotal());
            jr.getPageInfo().setPageNum(p.getPageNum());
            jr.getPageInfo().setPageSize(p.getPageSize());
            return ResponseEntity.ok(jr);
        }else {
            return ResponseEntity.ok(JsonResult.fail("查询失败"));
        }
    }
    @DeleteMapping
    public ResponseEntity<JsonResult> deleteReserve(@RequestBody Integer[] ids) {
        // TODO:修改为软删除
        int count = reserveService.deleteReserveByIds(ids);
        if(count > 0){
            return ResponseEntity.ok(JsonResult.success("成功删除"+count+"条数据",null));
        }else {
            return ResponseEntity.ok(JsonResult.fail("删除失败"));
        }
    }
    @PutMapping
    public ResponseEntity<JsonResult> updateReserve(@RequestBody Reserve reserve) {
        boolean success = reserveService.updateReserve(reserve);
        if(success){
            return ResponseEntity.ok(JsonResult.success("修改成功",null));
        }else {
            return ResponseEntity.ok(JsonResult.fail("修改失败"));
        }
    }
}
