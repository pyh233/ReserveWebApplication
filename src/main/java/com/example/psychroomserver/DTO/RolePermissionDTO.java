package com.example.psychroomserver.DTO;

import com.example.psychroomserver.model.chown.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RolePermissionDTO {
    Role role;
    public Integer[] selectedPermissions;
}
