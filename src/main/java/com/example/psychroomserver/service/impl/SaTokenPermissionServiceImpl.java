package com.example.psychroomserver.service.impl;

import cn.dev33.satoken.stp.StpInterface;
import com.example.psychroomserver.model.Admin;
import com.example.psychroomserver.model.chown.Group;
import com.example.psychroomserver.model.chown.Role;
import com.example.psychroomserver.service.AdminService;
import com.example.psychroomserver.service.RoleService;
import com.example.psychroomserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * sa-token通过此类获取当前用户所有的权限
 */
@Service
public class SaTokenPermissionServiceImpl implements StpInterface {
    private AdminService adminService;

    @Autowired
    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }

    // NOTE:获取当前用户的权限列表
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        List<Role> roleList = findAllRolesByAid(Integer.parseInt(loginId.toString()));
        Set<String> permissions = new HashSet<>();
        for (Role role : roleList) {
            permissions.addAll(role.getPermissions().stream().map(permission -> permission.getName()).toList());
        }
        return new ArrayList<>(permissions);
    }

    // NOTE:获取当前用户的角色列表
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        List<Role> roleList = findAllRolesByAid(Integer.valueOf(loginId.toString()));
        return roleList.stream().map(Role::getName).toList();
    }

    // NOTE:获取指定用户的所有角色
    private List<Role> findAllRolesByAid(Integer id) {
        Admin admin = adminService.findAdminById(id);
        Set<Role> roles = new HashSet<>();
        if (admin != null) {
            if (!admin.getGroupList().isEmpty()) {
                List<Group> groups = admin.getGroupList();
                for (Group group : groups) {
                    roles.addAll(group.getRoleList());
                }
            }
        }
        return new ArrayList<>(roles);
    }
}
