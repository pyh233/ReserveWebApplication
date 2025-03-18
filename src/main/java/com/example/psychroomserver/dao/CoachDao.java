package com.example.psychroomserver.dao;

import com.example.psychroomserver.model.Coach;
import com.example.psychroomserver.model.search.CoachSearchModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CoachDao {
    List<Coach> findAllCoach(CoachSearchModel csm);
    int deleteCoachByIds(Integer[] ids);
    int insertCoach(Coach coach);
    int updateCoach(Coach coach);

    Coach findCoachById(Integer id);
    List<Coach> getCoachList();
}
