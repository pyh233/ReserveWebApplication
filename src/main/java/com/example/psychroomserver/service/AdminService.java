package com.example.psychroomserver.service;

import com.example.psychroomserver.model.Admin;

public interface AdminService {
    public Admin findAdminByPhone(String phone);

    public Admin findAdminById(Integer id);
}
