package com.example.psychroomserver.dao;

import com.example.psychroomserver.config.MyBatisRedisCache;
import com.example.psychroomserver.model.Reserve;
import com.example.psychroomserver.model.ReserveStatus;
import com.example.psychroomserver.model.search.ReserveSearchModel;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
//@CacheNamespace(implementation= MyBatisRedisCache.class)
public interface ReserveDao {
    public List<Reserve> getReserve(ReserveSearchModel reserveSearchModel);
    public int addReserve(Reserve reserve);
    public int deleteReserveByIds(Integer[] ids);
    public int updateReserveStatus(ReserveStatus reserveStatus);

    public List<Reserve> getAllReserveByUid(Integer uid);
}
