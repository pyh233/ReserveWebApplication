package com.example.psychroomserver.service.impl;

import com.example.psychroomserver.dao.PermissionDao;
import com.example.psychroomserver.model.chown.Permission;
import com.example.psychroomserver.model.search.PermissionSearchModel;
import com.example.psychroomserver.service.PermissionService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {
    private PermissionDao permissionDao;
    private RedisTemplate redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

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
    public List<Permission> findAllPermissions(PermissionSearchModel permissionSearchModel, Page<?> page) {
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

    /**
     * 只是创建权限，添加资源在修改中添加
     *
     * @param permission
     * @return
     */
    @Override
    public boolean addPermission(Permission permission) {
        return permissionDao.addPermission(permission) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean renewRoutes4Permission(Integer pid, Integer[] rids) {
        //1.delete
        permissionDao.deleteAllRoute4Permission(pid);
        //2.add
        for (Integer rid : rids) {
            permissionDao.addLatestRoute4Permission(pid, rid);
        }
        //3.todo:flush cache
        redisTemplate.delete("com.example.psychroomserver.dao.RouteDao");
        return true;
    }

    /**
     * 列表使用
     *
     * @return
     */
    @Override
    public List<Permission> getAllPermissions() {
        return permissionDao.getAllPermissions();
    }


}
