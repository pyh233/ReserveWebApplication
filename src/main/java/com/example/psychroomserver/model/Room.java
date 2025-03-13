package com.example.psychroomserver.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class Room implements Serializable {
    private Integer id;
    private String name;
    private Integer maxCount;
}
