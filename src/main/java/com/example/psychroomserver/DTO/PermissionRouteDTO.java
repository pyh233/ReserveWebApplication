package com.example.psychroomserver.DTO;

import com.example.psychroomserver.model.chown.Permission;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PermissionRouteDTO {
    private Permission permission;
    private Integer[] selectedRoutes;
}
