package com.example.psychroomserver.dao;

import com.example.psychroomserver.model.chown.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoleDao {
    List<Role> findRoleByGroupId(Integer gid);
}
