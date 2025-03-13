package com.example.psychroomserver.service;

import com.example.psychroomserver.model.User;
import com.example.psychroomserver.model.search.UserSearchModel;
import com.github.pagehelper.Page;

import java.util.List;

public interface UserService {
    public List<User> getAllUsers(UserSearchModel user, Page<?> page);
    public int deleteUserByIds(Integer[] ids);
    public boolean addUser(User user);
    public boolean updateUser(User user);
}
