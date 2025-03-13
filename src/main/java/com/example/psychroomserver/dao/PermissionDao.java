package com.example.psychroomserver.dao;

import com.example.psychroomserver.model.chown.Permission;
import com.example.psychroomserver.model.search.PermissionSearchModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PermissionDao {
    /**
     * 级联查询链使用
     * @param rid
     * @return
     */
    List<Permission> findPermissionByRoleId(Integer rid);

    /**
     * satoken使用
     * @return
     */
    List<Permission> findAllPermission();

    List<Permission> findAllPermissions(PermissionSearchModel rsm);
    int deletePermissionByIds(Integer[] ids);
    int addPermission(Permission permission);
}
