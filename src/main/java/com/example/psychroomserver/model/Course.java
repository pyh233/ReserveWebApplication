package com.example.psychroomserver.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
// NOTE:如果往数据库中存放默认使用jdk序列化器需要实现serializable接口
public class Course implements Serializable {
    private Integer id;
    private String name;
    private LocalDateTime time;
    private Integer length;
    private String photo;
    private Integer coachId;
    private Integer roomId;

    // 教室规定为一对一，实际情况下可能存在更改教室，只需要更改roomId就可以了
    // TODO:
    private Coach coach;
    private Room room;
}
