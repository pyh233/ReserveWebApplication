package com.example.psychroomserver.Interceptor;

import com.alibaba.fastjson2.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.psychroomserver.util.JsonResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;
@Deprecated(since = "2025/03/11")
public class AdminLoginInterceptor implements HandlerInterceptor {

    private String secretKey;
    public AdminLoginInterceptor(String secretKey) {
        this.secretKey = secretKey;
    }
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String jwt = request.getHeader("Authorization");
//        // 校验jwt
//        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secretKey)).build();
//        // 如果令牌错误，解密会抛出异常
//        try{
                // NOTE:引入sa-token以后只需要StpUtil.checkLogin就可以检查登陆状态
//            DecodedJWT jwtDecoded = jwtVerifier.verify(jwt);
//            // 取出令牌中的用户信息，检查用户是否在黑名单
//            //
//            return true;
//        }catch (Exception e){
//            // 如果是用户删除jwt可以加入黑名单
                // NOTE:引入sa-token以后这个返回信息直接用异常处理类往前端返回信息
////            throw new RuntimeException("令牌校验失败");
//            response.setContentType("application/json;charset=utf-8");
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            JsonResult jr = JsonResult.fail("身份认证失败");
//
//            PrintWriter writer = response.getWriter();
//            response.getWriter().write(JSON.toJSONString(jr));
//            writer.flush();
//            return false;
//        }
//    }
}
