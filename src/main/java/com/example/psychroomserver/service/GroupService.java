package com.example.psychroomserver.service;

import com.example.psychroomserver.model.chown.Group;
import com.example.psychroomserver.model.search.GroupSearchModel;
import com.github.pagehelper.Page;

import java.util.List;

public interface GroupService {
    public List<Group> findAllGroups(GroupSearchModel groupSearchModel, Page<?> page);
    public int deleteGroupByIds(Integer[] ids);
    public boolean addGroup(Group group);
    public boolean renewRoles4Group(Integer groupId,Integer[] ids);

    public List<Group> getAllGroups();
}
