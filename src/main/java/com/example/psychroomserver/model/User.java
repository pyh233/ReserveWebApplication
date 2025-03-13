package com.example.psychroomserver.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class User implements Serializable {
    Integer id;
    String phone;
    String password;
    String name;
    LocalDateTime createTime;
    Boolean isDel;
}
