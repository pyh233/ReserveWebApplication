package com.example.psychroomserver.api;

import com.example.psychroomserver.util.JsonResult;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@RestController
@RequestMapping(value = "/static", produces = MediaType.APPLICATION_JSON_VALUE)
public class UploadApi {
    @Value("${custom.img.upload.location}")
    String uploadLocation;
    RedisTemplate redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("/image/upload/{type}")
    public ResponseEntity<JsonResult> uploadAvatar(MultipartFile file,
                                                   @PathVariable String type) throws IOException {
        if (!StringUtils.hasText(type)) {
            type = "unknown";
        }
        File destination = new File(uploadLocation + type);
        if (!destination.exists()) {
            destination.mkdirs();
        }
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

        return ResponseEntity.ok(JsonResult.success("sc", File.separator + type + "/" + fileName));
    }

    @DeleteMapping(value = "/image/delete")
    public ResponseEntity<JsonResult> deleteAvatar(@RequestBody String path) {
        if (!StringUtils.hasText(path)) {
            return ResponseEntity.ok(JsonResult.fail("照片取消上传"));
        }
        File destination = new File(uploadLocation + path);
        if (!destination.exists()) {
            return ResponseEntity.ok(JsonResult.fail("图片已不存在"));
        }
        destination.delete();
        return ResponseEntity.ok(JsonResult.success("已删除图片", null));
    }
}
