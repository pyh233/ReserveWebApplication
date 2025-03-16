package com.example.psychroomserver.service.impl;

import com.example.psychroomserver.dao.RouteDao;
import com.example.psychroomserver.model.chown.Route;
import com.example.psychroomserver.model.search.RouteSearchModel;
import com.example.psychroomserver.service.RouteService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteServiceImpl implements RouteService {
    RouteDao routeDao;

    @Autowired
    public void setRouteDao(RouteDao routeDao) {
        this.routeDao = routeDao;
    }

    @Override
    public List<Route> findAllRoutes(RouteSearchModel routeSearchModel, Page<?> page) {
        try (Page<?> __ = PageHelper.startPage(page.getPageNum(), page.getPageSize())) {
            return routeDao.findAllRoutes(routeSearchModel);
        }
    }

    @Override
    public int deleteRouteByIds(Integer[] ids) {
        if (ids == null || ids.length == 0) {
            return 0;
        }
        return routeDao.deleteRouteByIds(ids);
    }

    @Override
    public boolean addRoute(Route route) {
        return routeDao.addRoute(route) > 0;
    }

    @Override
    public boolean updateRoute(Route route) {
        return routeDao.updateRoute(route) > 0;
    }

    /**
     * 前端列表使用
     * @return
     */
    @Override
    public List<Route> getAllRoutes() {
        return routeDao.findAll();
    }

}
