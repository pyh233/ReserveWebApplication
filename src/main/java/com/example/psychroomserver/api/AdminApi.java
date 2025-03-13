package com.example.psychroomserver.api;

import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.psychroomserver.model.Admin;
import com.example.psychroomserver.service.AdminService;
import com.example.psychroomserver.util.JsonResult;
import org.apache.ibatis.annotations.Delete;
import org.jasypt.util.password.PasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/admin", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminApi {
    private AdminService adminService;
    private PasswordEncryptor pe = new StrongPasswordEncryptor();
    private RedisTemplate<Object, Object> redisTemplate;

    /*密钥，在配置文件中配置。存储在服务端，保密，可定期更换*/
//    @Value("${jwt.secret.key}")
//    private String secretKey;

    @Autowired
    public void setRedisTemplate(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/login")
    public ResponseEntity<JsonResult> login(@RequestBody Admin loginAdmin) {
        // 数据验证
        if (!StringUtils.hasText(loginAdmin.getPhone())) {
            throw new IllegalArgumentException("用户名不可为空");
        }
        //验证码验证
        String captcha = (String) redisTemplate.opsForValue().get("captcha-" + loginAdmin.getCaptchaCode());
        if (!loginAdmin.getCaptcha().equalsIgnoreCase(captcha)) {
            return ResponseEntity.ok(JsonResult.fail("验证码错误"));
        }
        Admin adminInDB = adminService.findAdminByPhone(loginAdmin.getPhone());
        if (adminInDB == null) {
            return ResponseEntity.ok(JsonResult.fail("用户不存在"));
        }
        try {
//            if (this.pe.checkPassword(loginAdmin.getPassword(), adminInDB.getPassword())) {
            if (true) {
                //颁发jwt
//                String jwt = JWT.create()
//                        .withAudience(adminInDB.getPhone())   //用户
//                        .withIssuedAt(Instant.now())   //签发时间
//                        .withExpiresAt(LocalDateTime.now().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant())    //过期时间
//                        .sign(Algorithm.HMAC256(secretKey));  //使用hmac256进行签名
//                redisTemplate.opsForValue().set("jwt",jwt);
                // NOTE:sa-token引入后 会自动颁发JWT并存入redis
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

    @DeleteMapping("/logout")
    public ResponseEntity<JsonResult> logout(@RequestBody String token) {
        // NOTE:这里不需要传递token，stp接收到的cookie就能帮我们登出
        StpUtil.logout();
        return ResponseEntity.ok(JsonResult.success("登出成功，即将跳转到登陆页面",null));
    }
}
