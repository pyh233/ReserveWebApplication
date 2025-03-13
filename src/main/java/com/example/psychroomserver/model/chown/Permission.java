package com.example.psychroomserver.model.chown;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Permission implements Serializable {
    @EqualsAndHashCode.Include
    private Integer id;
    private String name;

    private List<Route> routeList;
}
