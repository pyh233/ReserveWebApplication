package com.example.psychroomserver.model;

import com.example.psychroomserver.model.chown.Group;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@ToString
public class Admin implements Serializable {
    private Integer id;
    private String phone;
    private String password;
    private String name;

    private String captcha;
    private String captchaCode;

    private List<Group> groupList;
}
