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
public class Role implements Serializable {
    @EqualsAndHashCode.Include
    private Integer id;
    private String name;

    private List<Permission> permissions;
}
