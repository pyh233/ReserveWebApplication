package com.example.psychroomserver.model.search;

import com.example.psychroomserver.model.chown.Role;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(callSuper = true)
public class RoleSearchModel extends Role {
    private String nameLike;
}
