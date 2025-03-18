package com.example.psychroomserver.api;

import com.example.psychroomserver.model.Coach;
import com.example.psychroomserver.model.search.CoachSearchModel;
import com.example.psychroomserver.service.CoachService;
import com.example.psychroomserver.util.JsonResult;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/coach", produces = MediaType.APPLICATION_JSON_VALUE)
public class CoachApi {
    CoachService coachService;

    @Autowired
    public void setCoachService(CoachService coachService) {
        this.coachService = coachService;
    }

    @GetMapping
    public ResponseEntity<JsonResult> getAllCoach(CoachSearchModel coachSearchModel,
                                                  @RequestParam(defaultValue = "1") Integer page,
                                                  @RequestParam(defaultValue = "10") Integer limit) {
        Page<?> p = new Page<>(page, limit);
        List<Coach> coachList = coachService.getCoaches(coachSearchModel,p);
        PageInfo<Coach> pi = new PageInfo(coachList);
        if(!coachList.isEmpty()){
            JsonResult jr = JsonResult.success("success", coachList);
            jr.getPageInfo().setTotal(pi.getTotal());
            jr.getPageInfo().setPageNum(pi.getPageNum());
            jr.getPageInfo().setPageSize(pi.getPageSize());
            return ResponseEntity.ok(jr);
        }else {
            return ResponseEntity.ok(JsonResult.fail("查询失败"));
        }
    }
    @DeleteMapping
    public ResponseEntity<JsonResult> deleteCoach(@RequestBody Integer[] ids) {
        int count = coachService.deleteCoachByIds(ids);
        if(count > 0){
            return ResponseEntity.ok(JsonResult.success("成功删除"+count+"条数据",null));
        }else {
            return ResponseEntity.ok(JsonResult.fail("删除失败"));
        }
    }
    @PostMapping
    public ResponseEntity<JsonResult> addCoach(@RequestBody Coach coach) {
        boolean success = coachService.addCoach(coach);
        if(success){
            return ResponseEntity.ok(JsonResult.success("添加成功",null));
        }else {
            return ResponseEntity.ok(JsonResult.fail("添加失败"));
        }
    }
    @PutMapping
    public ResponseEntity<JsonResult> updateCoach(@RequestBody Coach coach) {
        boolean success = coachService.updateCoach(coach);
        if(success){
            return ResponseEntity.ok(JsonResult.success("修改成功",null));
        }else {
            return ResponseEntity.ok(JsonResult.fail("修改失败"));
        }
    }
    @GetMapping("/list")
    public ResponseEntity<JsonResult> getCoaches() {
        return ResponseEntity.ok(JsonResult.success("sc",coachService.getCoachList()));
    }
}
