package com.example.psychroomserver.service.impl;

import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.example.psychroomserver.dao.AdminDao;
import com.example.psychroomserver.model.Admin;
import com.example.psychroomserver.model.search.AdminSearchModel;
import com.example.psychroomserver.service.AdminService;
import com.example.psychroomserver.util.JsonResult;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.jasypt.util.password.PasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import java.util.List;
import java.util.Map;

@Service
@CacheConfig(cacheNames = "c.e.p.s.i.AdminServiceImpl")
public class AdminServiceImpl implements AdminService {
    private AdminDao adminDao;
    private PasswordEncryptor pe = new StrongPasswordEncryptor();
    private RedisTemplate redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setAdminDao(AdminDao adminDao) {
        this.adminDao = adminDao;
    }

    /**
     * 根据账号查询用户，登陆时调用
     * 只有用户信息
     * NOTE:个人不理解这个地方加缓存的作用。
     * @param phone
     * @return
     */
    @Override
    @Cacheable(keyGenerator = "myKeyGenerator")
    public ResponseEntity<JsonResult> loginAdmin(Admin loginAdmin) {
        // 验证码验证
        String captcha = (String) redisTemplate.opsForValue().get("captcha-" + loginAdmin.getCaptchaCode());
        if (!loginAdmin.getCaptcha().equalsIgnoreCase(captcha)) {
            return ResponseEntity.ok(JsonResult.fail("验证码错误"));
        }
        Admin adminInDB = adminDao.findAdminByPhone(loginAdmin.getPhone());
        if (adminInDB == null) {
            return ResponseEntity.ok(JsonResult.fail("用户不存在"));
        }
        try {
            if (pe.checkPassword(loginAdmin.getPassword(), adminInDB.getPassword())) {
//            if (true) {
                //颁发jwt
//                String jwt = JWT.create()
//                        .withAudience(adminInDB.getPhone())   //用户
//                        .withIssuedAt(Instant.now())   //签发时间
//                        .withExpiresAt(LocalDateTime.now().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant())    //过期时间
//                        .sign(Algorithm.HMAC256(secretKey));  //使用hmac256进行签名
//                redisTemplate.opsForValue().set("jwt",jwt);
                // NOTE:sa-token引入后 会自动颁发JWT并存入redis
                // WHY:LOGINID不可为空
                StpUtil.login(adminInDB.getId(), SaLoginConfig.setExtra("user", adminInDB));
                SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
                return ResponseEntity.ok(JsonResult.success("success", Map.of("jwt", tokenInfo.getTokenValue())));
            } else {
                return ResponseEntity.ok(JsonResult.fail("用户名或密码不正确"));
            }
        } catch (Exception e) {
            return ResponseEntity.ok(JsonResult.fail("密码校验失败"));
        }
    }

    /**
     * 根据id查询用户权限链
     * NOTE:用户身份变更需要清除这个地方的缓存
     * NOTE:身份变更调用renewGroup加注解清除缓存
     * @param id
     * @return
     */
    @Override
    @Cacheable(keyGenerator = "myKeyGenerator")
    public Admin findAdminById(Integer id) {
        return adminDao.findAdminById(id);
    }

    @Override
    public List<Admin> findAllAdmins(AdminSearchModel adminSearchModel, Page<?> page) {
        try (Page<?> __ = PageHelper.startPage(page.getPageNum(), page.getPageSize())) {
            return adminDao.findAllAdmins(adminSearchModel);
        }
    }

    @Override
    public int deleteAdminByIds(Integer[] ids) {
        if (ids == null || ids.length == 0) {
            return 0;
        }
        return adminDao.deleteAdminByIds(ids);
    }

    @Override
    public boolean updateAdmin(Admin admin) {
        Admin adminInDB = adminDao.findAdminById(admin.getId());
        if(!admin.getPassword().equals("")) {
            admin.setPassword(pe.encryptPassword(admin.getPassword()));
        }
        return adminDao.updateAdmin(admin) > 0;
    }

    @Override
    public boolean addAdmin(Admin admin) {
        admin.setPassword(pe.encryptPassword(admin.getPassword()));
        return adminDao.insertAdmin(admin) > 0;
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean renewGroup4Admin(Integer aid, Integer[] ids) {
        //1.delete
        adminDao.deleteGroup4Admin(aid);
        //2.insert
        for (Integer id : ids) {
            adminDao.insertGroup4Admin(aid, id);
        }
        return true;
    }

}
