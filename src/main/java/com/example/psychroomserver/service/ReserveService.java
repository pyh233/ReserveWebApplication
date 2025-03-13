package com.example.psychroomserver.service;

import com.example.psychroomserver.model.Reserve;
import com.example.psychroomserver.model.search.ReserveSearchModel;
import com.github.pagehelper.Page;

import java.util.List;

public interface ReserveService {
    public List<Reserve> getAllReserve(ReserveSearchModel reserveSearchModel, Page<?> page);
    public int deleteReserveByIds(Integer[] ids);
    public boolean addReserve(Reserve reserve);
    public boolean updateReserve(Reserve reserve);

    public List<Reserve> getAllReserveByUid(Integer uid);
}
