package com.example.psychroomserver.model.search;

import com.example.psychroomserver.model.chown.Route;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class RouteSearchModel extends Route {
    String nameLike;
}
