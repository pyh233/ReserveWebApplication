package com.example.psychroomserver;

import com.example.psychroomserver.model.ReserveStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.time.LocalDateTime;


@Getter
@Setter
public class ReserveSearchModel extends Reserve {

    @Test
    public void testExtends(){
        ReserveSearchModel reserveSearchModel = new ReserveSearchModel();
        System.out.println(reserveSearchModel);
    }
}
@Setter
@Getter
@ToString
class Reserve {
    private Integer id;
    private ReserveStatus status;
    private String comment;
    private LocalDateTime commentTime;
    private LocalDateTime reserveTime;
    private Integer courseId;
    private Integer userId;

    //TODO:查询---
}
