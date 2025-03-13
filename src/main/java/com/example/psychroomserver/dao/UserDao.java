package com.example.psychroomserver.dao;

import com.example.psychroomserver.model.User;
import com.example.psychroomserver.model.search.UserSearchModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserDao {
    public List<User> getUsers(UserSearchModel userSearchModel);
    public int addUser(User user);
    public int deleteUserByIds(Integer[] ids);
    public int updateUser(User user);

    public User getUserById(Integer id);
}
