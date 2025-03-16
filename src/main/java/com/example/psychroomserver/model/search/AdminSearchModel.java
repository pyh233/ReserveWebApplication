package com.example.psychroomserver.model.search;

import com.example.psychroomserver.model.Admin;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AdminSearchModel extends Admin {
    String nameLike;
}
