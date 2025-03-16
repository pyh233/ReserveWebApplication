package com.example.psychroomserver.dao;

import com.example.psychroomserver.model.Admin;
import com.example.psychroomserver.model.search.AdminSearchModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminDao {
    /**
     * 登陆时使用s
     * @param phone
     * @return
     */
    Admin findAdminByPhone(String phone);

    /**
     * 查询当前用户的权限链
     * @param id
     * @return
     */
    Admin findAdminById(Integer id);

    List<Admin> findAllAdmins(AdminSearchModel adminSearchModel);
    int deleteAdminByIds(Integer[] ids);
    int insertAdmin(Admin admin);
    int updateAdmin(Admin admin);

    /**
     * 修改用户的所有用户组
     */
    int deleteGroup4Admin(Integer aid);
    int insertGroup4Admin(Integer aid,Integer gid);
}
