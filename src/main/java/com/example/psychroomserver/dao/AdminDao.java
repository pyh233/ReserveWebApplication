package com.example.psychroomserver.dao;

import com.example.psychroomserver.model.Admin;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminDao {
    Admin findAdminByPhone(String phone);
    Admin findAdminById(Integer id);
}
