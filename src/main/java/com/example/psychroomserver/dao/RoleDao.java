package com.example.psychroomserver.dao;

import com.example.psychroomserver.model.chown.Role;
import com.example.psychroomserver.model.search.RoleSearchModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoleDao {
    List<Role> findRoleByGroupId(Integer gid);


    List<Role> findAllRoles(RoleSearchModel rsm);
    int deleteRoleByIds(Integer[] ids);
    int insertRole(Role role);


    /**
     * @改变角色拥有的权限
     * @param roleId
     * @return
     */
    int deleteAllPermission4Role(Integer roleId);
    int insertNewPermission4Role(Integer roleId, Integer permissionId);


    /**
     * 列表使用
     */

    List<Role> getAllRoles();
}
