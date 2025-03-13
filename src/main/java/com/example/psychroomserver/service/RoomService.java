package com.example.psychroomserver.service;

import com.example.psychroomserver.model.Room;
import com.example.psychroomserver.model.search.RoomSearchModel;
import com.github.pagehelper.Page;

import java.util.List;

public interface RoomService {
    public List<Room> getAllRooms(RoomSearchModel roomSearchModel, Page<?> page);
    public int deleteRoom(Integer[] ids);
    public boolean addRoom(Room room);
    public boolean updateRoom(Room room);
}
