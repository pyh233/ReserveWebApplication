package com.example.psychroomserver.service.impl;

import com.example.psychroomserver.dao.PermissionDao;
import com.example.psychroomserver.model.chown.Permission;
import com.example.psychroomserver.model.search.PermissionSearchModel;
import com.example.psychroomserver.service.PermissionService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {
    private PermissionDao permissionDao;

    @Autowired
    public void setPermissionDao(PermissionDao permissionDao) {
        this.permissionDao = permissionDao;
    }

    /**
     * @return
     * @sa-token使用的函数
     */
    @Override
    public List<Permission> findAll() {
        return permissionDao.findAllPermission();
    }

    @Override
    public List<Permission> getPermissions(PermissionSearchModel permissionSearchModel, Page<?> page) {
        try (Page<?> __ = PageHelper.startPage(page.getPageNum(), page.getPageSize())) {
            return permissionDao.findAllPermissions(permissionSearchModel);
        }
    }

    @Override
    public int deletePermissionByIds(Integer[] ids) {
        if (ids == null || ids.length == 0) {
            return 0;
        }
        return permissionDao.deletePermissionByIds(ids);
    }

    @Override
    public boolean addPermission(Permission permission) {
        return permissionDao.addPermission(permission) > 0;
    }
}
