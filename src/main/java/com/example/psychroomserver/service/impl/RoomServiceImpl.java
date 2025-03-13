package com.example.psychroomserver.service.impl;

import com.example.psychroomserver.dao.RoomDao;
import com.example.psychroomserver.model.Room;
import com.example.psychroomserver.model.search.RoomSearchModel;
import com.example.psychroomserver.service.RoomService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "c.e.p.s.i.RoomServiceImpl")
public class RoomServiceImpl implements RoomService {
    RoomDao roomDao;

    @Autowired
    public void setRoomDao(RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    @Override
    public List<Room> getAllRooms(RoomSearchModel searchModel, Page<?> page) {
        try (Page __ = PageHelper.startPage(page.getPageNum(), page.getPageSize())) {
            return roomDao.getAllRooms(searchModel);
        }
    }

    @Override
    @CacheEvict(allEntries = true)
    public int deleteRoom(Integer[] ids) {
        return roomDao.deleteRoomByIds(ids);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean addRoom(Room room) {
        return roomDao.addRoom(room) > 0;
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean updateRoom(Room room) {
        return roomDao.updateRoom(room) > 0;
    }
}
