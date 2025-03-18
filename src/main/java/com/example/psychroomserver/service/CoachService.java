package com.example.psychroomserver.service;

import com.example.psychroomserver.model.Coach;
import com.example.psychroomserver.model.search.CoachSearchModel;
import com.github.pagehelper.Page;

import java.util.List;

public interface CoachService {
    List<Coach> getCoaches(CoachSearchModel coachSearchModel, Page<?> page);
    int deleteCoachByIds(Integer[] ids);
    boolean addCoach(Coach coach);
    boolean updateCoach(Coach coach);

    List<Coach> getCoachList();
}
