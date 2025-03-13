package com.example.psychroomserver.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.*;
import java.time.Duration;
import java.util.UUID;

@Controller
@RequestMapping(value = "/static",produces = MediaType.IMAGE_PNG_VALUE)
public class ImageLoaderController {
    @Value("${custom.img.upload.location}")
    private String uploadLocation;
    @GetMapping("/{folder}/{fileName}")
    public void imageLoader(@PathVariable String folder,
                              @PathVariable String fileName, HttpServletResponse response) {
        response.setContentType("image/gif");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0L);
        // 每次发送请求验证码都会随机生成一个key，并且把答案作为value存入Redis，再将keykey放入响应头
        File file = new File(uploadLocation + File.separator + folder + File.separator + fileName);

        try (InputStream in = new FileInputStream(file);
             OutputStream out = response.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);  // 返回 500
        }
    }

}
