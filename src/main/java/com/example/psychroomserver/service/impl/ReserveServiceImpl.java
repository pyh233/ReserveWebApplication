package com.example.psychroomserver.service.impl;

import com.example.psychroomserver.dao.CourseDao;
import com.example.psychroomserver.dao.ReserveDao;
import com.example.psychroomserver.dao.UserDao;
import com.example.psychroomserver.model.Reserve;
import com.example.psychroomserver.model.search.ReserveSearchModel;
import com.example.psychroomserver.service.ReserveService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "c.e.p.s.i.ReserveServiceImpl")
public class ReserveServiceImpl implements ReserveService {
    ReserveDao reserveDao;
    CourseDao courseDao;
    UserDao userDao;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setCourseDao(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    @Autowired
    public void setReserveDao(ReserveDao reserveDao) {
        this.reserveDao = reserveDao;
    }

    @Override
    // NOTE:不能缓存因为pagehelper不带有total
    public List<Reserve> getAllReserve(ReserveSearchModel reserveSearchModel, Page<?> page) {
        try (Page<?> __ = PageHelper.startPage(page.getPageNum(), page.getPageSize())) {
            List<Reserve> reserves = reserveDao.getReserve(reserveSearchModel);
            // WHY:热部署+级联查询 Mybatis层报错，无法将缓存转化为User和Course
            // WHY:热部署+多次查询 SpringCache报错，无法将主类加载器和热部署类加载器的类识别为一个
            // WHY:上面两种情况虽然报错不同，但是其实是一种，因为都是DAO查出的数据不匹配
            // NOTE:热部署出现问题，考虑到级联查询快并且pagehelper还是会缓存每一页的数据
            // NOTE:并且page刚传到Service层的时候不带有total信息，将Service层中的缓存去掉，只使用Dao层缓存
            // NOTE:解决方案：关掉热部署/配置mybatisJSON数据序列化器
//            for (Reserve reserve : reserves) {
//                reserve.setUser(userDao.getUserById(reserve.getUserId()));
//                reserve.setCourse(courseDao.getCourseById(reserve.getCourseId()));
//            }
            return reserves;
        }
    }

    @Override
    @CacheEvict(allEntries = true)
    public int deleteReserveByIds(Integer[] ids) {
        if (ids == null || ids.length == 0) {
            return 0;
        }
        return reserveDao.deleteReserveByIds(ids);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean addReserve(Reserve reserve) {
        return reserveDao.addReserve(reserve) > 0;
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean updateReserve(Reserve reserve) {
        return reserveDao.updateReserveStatus(reserve.getStatus()) > 0;
    }

    @Override
    @Cacheable(keyGenerator = "myKeyGenerator")
    public List<Reserve> getAllReserveByUid(Integer uid) {
        return reserveDao.getAllReserveByUid(uid);
    }
}
