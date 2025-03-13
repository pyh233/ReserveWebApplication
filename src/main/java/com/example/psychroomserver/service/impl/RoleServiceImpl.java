package com.example.psychroomserver.service.impl;

import com.example.psychroomserver.dao.RoleDao;
import com.example.psychroomserver.model.chown.Role;
import com.example.psychroomserver.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private RoleDao roleDao;

    @Autowired
    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

}
