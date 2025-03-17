package com.example.psychroomserver.service.impl;

import com.example.psychroomserver.dao.RoleDao;
import com.example.psychroomserver.model.chown.Role;
import com.example.psychroomserver.model.search.RoleSearchModel;
import com.example.psychroomserver.service.RoleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private RoleDao roleDao;
    private RedisTemplate redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    public List<Role> findAllRoles(RoleSearchModel roleSearchModel, Page<?> page) {
        try (Page<?> __ = PageHelper.startPage(page.getPageNum(), page.getPageSize())) {
            return roleDao.findAllRoles(roleSearchModel);
        }
    }

    @Override
    public int deleteRoleByIds(Integer[] ids) {
        if (ids == null || ids.length == 0) {
            return 0;
        }
        return roleDao.deleteRoleByIds(ids);
    }

    @Override
    public boolean addRole(Role role) {
        return roleDao.insertRole(role) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean renewPermission4Role(Integer rid, Integer[] ids) {
        roleDao.deleteAllPermission4Role(rid);

        for (Integer id : ids) {
            roleDao.insertNewPermission4Role(rid, id);
        }
        redisTemplate.delete("com.example.psychroomserver.dao.PermissionDao");
        return true;
    }

    /**
     * 列表使用，为Group提供服务
     *
     * @return
     */
    @Override
    public List<Role> getAllRoles() {
        return roleDao.getAllRoles();
    }


}
