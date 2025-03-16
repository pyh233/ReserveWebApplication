package com.example.psychroomserver.DTO;

import com.example.psychroomserver.model.Admin;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AdminGroupDTO {
    private Admin admin;
    private Integer[] selectedGroups;
}
