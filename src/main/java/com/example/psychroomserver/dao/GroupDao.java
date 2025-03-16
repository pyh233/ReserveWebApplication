package com.example.psychroomserver.dao;

import com.example.psychroomserver.model.chown.Group;
import com.example.psychroomserver.model.search.GroupSearchModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GroupDao {
    public List<Group> findByAdminId(Integer adminId);

    List<Group> findAllGroups(GroupSearchModel groupSearchModel);
    int deleteGroupByIds(Integer[] ids);
    int addGroup(Group group);

    /**
     * @改变用户组所拥有的角色
     */
    int deleteRoles4Group(Integer groupId);
    int insertNewRoles4Group(Integer groupId,Integer roleId);

    /**
     * 列表使用
     */
    List<Group> getAllGroups();
}
