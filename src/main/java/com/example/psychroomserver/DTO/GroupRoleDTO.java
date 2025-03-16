package com.example.psychroomserver.DTO;

import com.example.psychroomserver.model.chown.Group;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class GroupRoleDTO {
    private Group group;
    private Integer[] selectedRoles;
}
