package com.example.psychroomserver.dao;

import com.example.psychroomserver.model.Course;
import com.example.psychroomserver.model.search.CourseSrarchModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CourseDao {
    public List<Course> getCourse(CourseSrarchModel courseSrarchModel);
    public int addCourse(Course course);
    public int deleteCourseByIds(Integer[] ids);
    public int updateCourse(Course course);

    public Course getCourseById(Integer id);
}
