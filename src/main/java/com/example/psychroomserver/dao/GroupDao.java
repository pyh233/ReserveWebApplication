package com.example.psychroomserver.dao;

import com.example.psychroomserver.model.chown.Group;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GroupDao {
    public List<Group> findByAdminId(Integer adminId);
}
