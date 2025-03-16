package com.example.psychroomserver.service.impl;

import com.example.psychroomserver.dao.GroupDao;
import com.example.psychroomserver.dao.PermissionDao;
import com.example.psychroomserver.model.chown.Group;
import com.example.psychroomserver.model.search.GroupSearchModel;
import com.example.psychroomserver.service.GroupService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {
    GroupDao groupDao;

    @Autowired
    public void setGroupDao(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    @Override
    public List<Group> findAllGroups(GroupSearchModel groupSearchModel, Page<?> page) {
        try (Page<?> __ = PageHelper.startPage(page.getPageNum(), page.getPageSize())) {
            return groupDao.findAllGroups(groupSearchModel);
        }
    }

    @Override
    public int deleteGroupByIds(Integer[] ids) {
        if (ids == null || ids.length == 0) {
            return 0;
        }
        return groupDao.deleteGroupByIds(ids);
    }

    @Override
    public boolean addGroup(Group group) {
        return groupDao.addGroup(group) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean renewRoles4Group(Integer groupId, Integer[] ids) {
        //1.delete
        groupDao.deleteRoles4Group(groupId);
        //2.renew
        for (Integer id : ids) {
            groupDao.insertNewRoles4Group(groupId, id);
        }
        return true;
    }

    @Override
    public List<Group> getAllGroups() {
        return groupDao.getAllGroups();
    }

}
