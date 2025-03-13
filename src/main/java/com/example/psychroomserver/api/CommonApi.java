package com.example.psychroomserver.api;

import com.example.psychroomserver.util.JsonResult;
import com.wf.captcha.SpecCaptcha;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/common")
public class CommonApi {
    private RedisTemplate<Object, Object> redisTemplate;
    @Value("${custom.img.upload.location}")
    private String uploadLocation;
    @Autowired
    public void setRedisTemplate(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping(value = "/captcha",produces = MediaType.IMAGE_PNG_VALUE)
    public void captcha(HttpServletResponse response, HttpServletRequest request) throws IOException {
        SpecCaptcha captcha = new SpecCaptcha(130, 48, 4);
        response.setContentType("image/gif");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0L);
        // 每次发送请求验证码都会随机生成一个key，并且把答案作为value存入Redis，再将keykey放入响应头
        String captchaCode = UUID.randomUUID().toString();
        redisTemplate.opsForValue()
                .set("captcha-" + captchaCode, captcha.text().toLowerCase(),
                        Duration.ofSeconds(90));

        response.setHeader("captcha-code", captchaCode);

        captcha.out(response.getOutputStream());
    }
    @PostMapping(value = "/upload/image",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonResult> uploadImage(@RequestParam("file") MultipartFile file,@RequestParam("type") String type) throws IOException {
        if(!StringUtils.hasText(type)) {
            type = "unknown";
        }
        File fileDir = new File(uploadLocation + type);
        if(!fileDir.exists()) {
            boolean success = fileDir.mkdirs();
            if(!success) {
                return ResponseEntity.ok(JsonResult.fail("上传图片失败"));
            }
        }
        // 获取拓展名(带点.)
        String fileName = file.getOriginalFilename();
        String ext = fileName.substring(fileName.lastIndexOf("."));
        // 改名
        fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        // 取随机数
        Random random = new Random();
        int n = random.nextInt(1000, 10000);
        // 拼好服务器上的名字
        fileName += '-' + n + ext;
        //
        File desc = new File(uploadLocation + type + File.separator + fileName);
        file.transferTo(desc);

        return ResponseEntity.ok(JsonResult.success("保存成功", type + "/" + fileName));
    }
    @DeleteMapping("/cancel")
    public ResponseEntity<JsonResult> cancelUpload(String fileUrl) throws IOException {
        File file = new File(uploadLocation + fileUrl);
        if(!file.exists()) {
            // TODO:输出到日志
            return ResponseEntity.ofNullable(JsonResult.fail("000"));
        }
        boolean success = file.delete();
        if(success) {
            // TODO:输出到日志
            return ResponseEntity.ok(JsonResult.success("000",null));
        }else{
            // TODO:输出到日志
            return ResponseEntity.ofNullable(JsonResult.fail("111"));
        }
    }
}
