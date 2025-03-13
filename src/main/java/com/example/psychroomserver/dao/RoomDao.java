package com.example.psychroomserver.dao;

import com.example.psychroomserver.model.Room;
import com.example.psychroomserver.model.search.RoomSearchModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoomDao {
    public List<Room> getAllRooms(RoomSearchModel roomSearchModel);
    public int deleteRoomByIds(Integer[] ids);
    public int addRoom(Room room);
    public int updateRoom(Room room);
}
