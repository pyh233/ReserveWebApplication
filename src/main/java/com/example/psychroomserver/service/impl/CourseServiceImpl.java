package com.example.psychroomserver.service.impl;

import com.example.psychroomserver.dao.CourseDao;
import com.example.psychroomserver.model.Course;
import com.example.psychroomserver.model.search.CourseSrarchModel;
import com.example.psychroomserver.service.CourseService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@CacheConfig(cacheNames = "c.e.p.s.i.CourseServiceImpl")
public class CourseServiceImpl implements CourseService {
    CourseDao courseDao;

    @Autowired
    public void setCourseDao(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    @Override
    public List<Course> getAllCourse(CourseSrarchModel courseSrarchModel, Page<?> page) {
        try (Page<?> __ = PageHelper.startPage(page.getPageNum(), page.getPageSize())) {
            return courseDao.getCourse(courseSrarchModel);
        }
    }

    @Override
    @CacheEvict(allEntries = true)
    public int deleteCourseByIds(Integer[] ids) {
        if (ids == null || ids.length == 0) {
            return 0;
        }
        return courseDao.deleteCourseByIds(ids);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean addCourse(Course course) {
        return courseDao.addCourse(course) > 0;
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean updateCourse(Course course) {
        return courseDao.updateCourse(course) > 0;
    }
}
