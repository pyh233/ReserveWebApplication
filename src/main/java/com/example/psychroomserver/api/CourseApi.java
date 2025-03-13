package com.example.psychroomserver.api;

import com.example.psychroomserver.model.Course;
import com.example.psychroomserver.model.search.CourseSrarchModel;
import com.example.psychroomserver.service.CourseService;
import com.example.psychroomserver.util.JsonResult;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/course")
public class CourseApi {
    CourseService courseService;

    @Autowired
    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<JsonResult> getCourse(@RequestParam(defaultValue = "1") Integer page,
                                              @RequestParam(defaultValue = "10") Integer limit,
                                              CourseSrarchModel courseSrarchModel) {
        Page<?> p = new Page<>(page, limit);
        List<Course> courseList = courseService.getAllCourse(courseSrarchModel, p);
        PageInfo pi = new PageInfo(courseList);
        if (!courseList.isEmpty()) {
            JsonResult jr = JsonResult.success("查询成功", courseList);
            jr.getPageInfo().setTotal(pi.getTotal());
            jr.getPageInfo().setPageNum(p.getPageNum());
            jr.getPageInfo().setPageSize(p.getPageSize());
            return ResponseEntity.ok(jr);
        } else {
            return ResponseEntity.ok(JsonResult.fail("查询失败"));
        }
    }

    @DeleteMapping
    public ResponseEntity<JsonResult> deleteCourse(@RequestBody Integer[] ids) {
        int count = courseService.deleteCourseByIds(ids);
        if (count > 0) {
            return ResponseEntity.ok(JsonResult.success("成功删除" + count + "条数据", null));
        } else {
            return ResponseEntity.ok(JsonResult.fail("删除失败"));
        }
    }

    @PostMapping
    public ResponseEntity<JsonResult> addCourse(@RequestBody Course course) {
        boolean success = courseService.addCourse(course);
        if (success) {
            return ResponseEntity.ok(JsonResult.success("添加成功", null));
        } else {
            return ResponseEntity.ok(JsonResult.fail("删除失败"));
        }
    }

    @PutMapping
    public ResponseEntity<JsonResult> updateCourse(@RequestBody Course course) {
        boolean success = courseService.updateCourse(course);
        if (success) {
            return ResponseEntity.ok(JsonResult.success("修改成功", null));
        } else {
            return ResponseEntity.ok(JsonResult.fail("修改失败"));
        }
    }
}
