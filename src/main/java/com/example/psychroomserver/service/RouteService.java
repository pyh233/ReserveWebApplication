package com.example.psychroomserver.service;

import com.example.psychroomserver.model.chown.Route;
import com.example.psychroomserver.model.search.RouteSearchModel;
import com.github.pagehelper.Page;

import java.util.List;

public interface RouteService {
    public List<Route> findAllRoutes(RouteSearchModel routeSearchModel, Page<?> page);
    public int deleteRouteByIds(Integer[] ids);
    public boolean addRoute(Route route);
    public boolean updateRoute(Route route);


    public List<Route> getAllRoutes();
}
