package com.example.psychroomserver.model.search;

import com.example.psychroomserver.model.chown.Permission;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class PermissionSearchModel extends Permission {
    String nameLike;
}
