package com.example.psychroomserver.service.impl;

import com.example.psychroomserver.dao.AdminDao;
import com.example.psychroomserver.model.Admin;
import com.example.psychroomserver.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;

@Service
@CacheConfig(cacheNames = "c.e.p.s.i.AdminServiceImpl")
public class AdminServiceImpl implements AdminService {
    AdminDao adminDao;

    @Autowired
    public void setAdminDao(AdminDao adminDao) {
        this.adminDao = adminDao;
    }

    /**
     * 根据账号查询用户，登陆时调用
     * 只有用户信息
     * @param phone
     * @return
     */
    @Override
    @Cacheable(keyGenerator = "myKeyGenerator")
    public Admin findAdminByPhone(String phone) {
        return adminDao.findAdminByPhone(phone);
    }

    /**
     * 根据id查询用户权限链
     * NOTE:用户身份变更需要清除这个地方的缓存
     * @param id
     * @return
     */
    @Override
//    @Cacheable(keyGenerator = "myKeyGenerator")
    public Admin findAdminById(Integer id) {
        return adminDao.findAdminById(id);
    }
}
