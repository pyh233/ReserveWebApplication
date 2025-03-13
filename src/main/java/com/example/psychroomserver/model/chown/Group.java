package com.example.psychroomserver.model.chown;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Group implements Serializable {
    @EqualsAndHashCode.Include
    private Integer id;
    private String name;

    private List<Role> roleList;
}
