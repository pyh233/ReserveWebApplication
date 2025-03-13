package com.example.psychroomserver.model.chown;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Route implements Serializable {
    @EqualsAndHashCode.Include
    private Integer id;
    private String name;
    private String url;
    private String type;
}
