package com.example.psychroomserver.service.impl;

import com.example.psychroomserver.dao.UserDao;
import com.example.psychroomserver.model.User;
import com.example.psychroomserver.model.search.UserSearchModel;
import com.example.psychroomserver.service.UserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@CacheConfig(cacheNames = "c.e.p.s.i.UserServiceImpl")
public class UserServiceImpl implements UserService {
    UserDao userDao;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<User> getAllUsers(UserSearchModel user, Page<?> page) {
        try (Page<?> __ = PageHelper.startPage(page.getPageNum(), page.getPageSize())) {
            return userDao.getUsers(user);
        }
    }

    @Override
    @CacheEvict(allEntries = true)
    public int deleteUserByIds(Integer[] ids) {
        if (ids == null || ids.length == 0) {
            return 0;
        }
        return userDao.deleteUserByIds(ids);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean addUser(User user) {
        user.setCreateTime(LocalDateTime.now());
        return userDao.addUser(user) > 0;
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean updateUser(User user) {
        return userDao.updateUser(user) > 0;
    }
}
