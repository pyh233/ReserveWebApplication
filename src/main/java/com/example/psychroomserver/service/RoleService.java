package com.example.psychroomserver.service;

import com.example.psychroomserver.model.chown.Role;
import com.example.psychroomserver.model.search.RoleSearchModel;
import com.github.pagehelper.Page;

import java.util.List;

public interface RoleService {
    public List<Role> findAllRoles(RoleSearchModel roleSearchModel, Page<?> page);
    public int deleteRoleByIds(Integer[] ids);
    public boolean addRole(Role role);
    public boolean renewPermission4Role(Integer rid,Integer[] ids);

    public List<Role> getAllRoles();
}
