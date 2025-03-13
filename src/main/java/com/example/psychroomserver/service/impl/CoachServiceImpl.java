package com.example.psychroomserver.service.impl;

import com.example.psychroomserver.dao.CoachDao;
import com.example.psychroomserver.model.Coach;
import com.example.psychroomserver.model.search.CoachSearchModel;
import com.example.psychroomserver.service.CoachService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "c.e.p.s.i.CoachServiceImpl")
public class CoachServiceImpl implements CoachService {
    CoachDao coachDao;

    @Autowired
    public void setCoachDao(CoachDao coachDao) {
        this.coachDao = coachDao;
    }

    @Override
    public List<Coach> getCoaches(CoachSearchModel coachSearchModel, Page<?> page) {
        try (Page<?> __ = PageHelper.startPage(page.getPageNum(), page.getPageSize())) {
            return coachDao.findAllCoach(coachSearchModel);
        }
    }

    @Override
    @CacheEvict(allEntries = true)
    public int deleteCoachByIds(Integer[] ids) {
        if (ids == null || ids.length == 0) {
            return 0;
        }
        return coachDao.deleteCoachByIds(ids);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean addCoach(Coach coach) {
        return coachDao.insertCoach(coach) > 0;
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean updateCoach(Coach coach) {
        return coachDao.updateCoach(coach) > 0;
    }
}
