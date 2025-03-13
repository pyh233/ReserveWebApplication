package com.example.psychroomserver.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
@Setter
@Getter
@ToString
@JsonIgnoreProperties("handler")
public class Reserve implements Serializable {
    private Integer id;
    private ReserveStatus status;
    private String comment;
    private LocalDateTime commentTime;
    private LocalDateTime reserveTime;
    private Integer courseId;
    private Integer userId;

    //TODO:查询---

    private Course course;
    private User user;
}
