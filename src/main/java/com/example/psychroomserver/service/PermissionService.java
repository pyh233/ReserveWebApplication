package com.example.psychroomserver.service;

import com.example.psychroomserver.model.chown.Permission;
import com.example.psychroomserver.model.chown.Permission;
import com.example.psychroomserver.model.search.PermissionSearchModel;
import com.github.pagehelper.Page;

import java.util.List;

public interface PermissionService {
    /**
     * findAll是sa-token查询用户所有角色或者所有权限的时候使用的函数
     * @return
     */
    public List<Permission> findAll();

    public List<Permission> findAllPermissions(PermissionSearchModel permissionSearchModel, Page<?> page);
    public int deletePermissionByIds(Integer[] ids);
    public boolean addPermission(Permission permission);
    public boolean renewRoutes4Permission(Integer pid,Integer[] rids);

    public List<Permission> getAllPermissions();
}
