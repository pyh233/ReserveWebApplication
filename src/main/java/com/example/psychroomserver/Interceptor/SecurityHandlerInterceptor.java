package com.example.psychroomserver.Interceptor;

import cn.dev33.satoken.stp.StpUtil;
import com.example.psychroomserver.model.chown.Permission;
import com.example.psychroomserver.model.chown.Route;
import com.example.psychroomserver.service.PermissionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.*;

/**
 * @satoken拦截器加入注释变成拦截器无法new，因此在配置中依赖注入
 */
@Component
public class SecurityHandlerInterceptor implements HandlerInterceptor {
    PermissionService permissionService;

    @Autowired
    public void setPermissionService(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        StpUtil.checkLogin();//NOTE:检查权限前先登陆状态，未登录抛出异常由异常处理结构返回未登录错误
        String requestURI = request.getRequestURI();// 请求路径
        String method = request.getMethod();// 请求类型
        // 1.访问当前uri需要什么权限
        PathMatcher matcher = new AntPathMatcher();
        boolean isMatch = false;
        //2.取出库中所有路径以及所需要的任意权限
        Map<Route, Set<Permission>> routeMap = findAllRoute2Permission();
        //3.开始判断请求路径是否匹配
        Set<Route> keySet = routeMap.keySet();
        for (Route route : keySet) {
            String url = route.getUrl();
            if (matcher.match(url, requestURI) &&
                    (route.getType().equals("all") || route.getType().equalsIgnoreCase(method))) {
                //如果匹配取出所有权限
                isMatch = true;
                Set<Permission> permissions = routeMap.get(route);
                for (Permission permission : permissions) {
                    if (StpUtil.hasPermission(permission.getName())) {
                        return true;
                    }
                }
            }
        }
        // 如果没有匹配路径，也就是其他路径不应该拦截
        if (!isMatch) {
            return true;
        }
        response.setStatus(HttpStatus.FORBIDDEN.value());

        return false;
    }

    // 获取所有路由/权限的映射
    private Map<Route, Set<Permission>> findAllRoute2Permission() {
        List<Permission> permissions = permissionService.findAll();// 查询的一对多数据
        // 新建一个要返回的数据
        Map<Route,Set<Permission>> routeMap = new HashMap<Route,Set<Permission>>();
        // 遍历所有权限中的所有route
        for(Permission permission : permissions) {
            List<Route> routes = permission.getRouteList();
            if(!routes.isEmpty()){
                // 遍历所有的route，根据route获取map中的所有权限
                for(Route route : routes) {
                    Set<Permission> set = routeMap.get(route);
                    // 如果是首次首次访问权限set，那么set为空，放入一个新建的set
                    if(set == null){
                        set = new HashSet<Permission>();
                        routeMap.put(route,set);
                    }
                    // 不是第一次访问就添加此权限
                    set.add(permission);
                }
            }
        }
        return routeMap;
    }
}
