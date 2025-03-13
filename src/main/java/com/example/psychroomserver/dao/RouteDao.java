package com.example.psychroomserver.dao;

import com.example.psychroomserver.model.chown.Permission;
import com.example.psychroomserver.model.chown.Route;
import com.example.psychroomserver.model.search.RouteSearchModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RouteDao {
    List<Route> findRouteByPid(Integer pid);

    List<Route> findAllRoutes(RouteSearchModel rsm);
    int deleteRouteByIds(Integer[] ids);
    int addRoute(Route route);
    int updateRoute(Route route);

    List<Route> findAll();
}
