package com.example.psychroomserver.service;

import com.example.psychroomserver.model.Course;
import com.example.psychroomserver.model.search.CourseSrarchModel;
import com.github.pagehelper.Page;

import java.util.List;

public interface CourseService {
    public List<Course> getAllCourse(CourseSrarchModel courseSrarchModel, Page<?> page);
    public int deleteCourseByIds(Integer[] ids);
    public boolean addCourse(Course course);
    public boolean updateCourse(Course course);
}
