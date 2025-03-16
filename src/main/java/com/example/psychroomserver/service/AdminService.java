package com.example.psychroomserver.service;

import com.example.psychroomserver.model.Admin;
import com.example.psychroomserver.model.search.AdminSearchModel;
import com.example.psychroomserver.util.JsonResult;
import com.github.pagehelper.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdminService {
    public ResponseEntity<JsonResult> loginAdmin(Admin loginAdmin);

    public Admin findAdminById(Integer id);

    public List<Admin> findAllAdmins(AdminSearchModel adminSearchModel, Page<?> page);
    public int deleteAdminByIds(Integer[] ids);
    public boolean updateAdmin(Admin admin);
    public boolean addAdmin(Admin admin);

    public boolean renewGroup4Admin(Integer aid,Integer[] ids);
}
