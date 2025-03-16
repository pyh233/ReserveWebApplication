package com.example.psychroomserver.dao;

import com.example.psychroomserver.model.chown.Permission;
import com.example.psychroomserver.model.search.PermissionSearchModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PermissionDao {
    /**
     * 级联查询链使用(查询用户当前拥有的权限)
     * @param rid
     * @return
     */
    List<Permission> findPermissionByRoleId(Integer rid);

    /**
     * sa-token使用(查询访问某一连接需要的权限)
     * @return
     */
    List<Permission> findAllPermission();

    List<Permission> findAllPermissions(PermissionSearchModel rsm);
    int deletePermissionByIds(Integer[] ids);
    int addPermission(Permission permission);

    /**
     * note：改变权限掌握的资源
     * @param pid
     * @return
     */
    int deleteAllRoute4Permission(Integer pid);
    int addLatestRoute4Permission(Integer pid,Integer rid);

    /**
     * 前端列表使用
     * @return
     */
    List<Permission> getAllPermissions();
}
