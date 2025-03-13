package com.example.psychroomserver.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@ToString
public class Coach implements Serializable {
    private Integer id;
    private String name;
    private String phone;
    private String wechat;
    private String recomm;
    private String photo;
}
