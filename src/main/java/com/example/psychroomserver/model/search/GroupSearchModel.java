package com.example.psychroomserver.model.search;

import com.example.psychroomserver.model.chown.Group;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(callSuper = true)
public class GroupSearchModel extends Group {
    String nameLike;
}
