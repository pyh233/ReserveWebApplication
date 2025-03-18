package com.example.psychroomserver;

import com.example.psychroomserver.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessResourceFailureException;

import java.time.Duration;

@FunctionalInterface
interface Task {
    public void doTask(Integer id);
}

class MyTask implements Task {

    @Override
    public void doTask(Integer id) {
        System.out.println("MyTask" + id);
    }
}

@SpringBootTest
public class LambdaTest {


    @DisplayName("测试")
    @Test
    public void TestFunc() {
        // 1.原始的
        Task t = new Task() {
            @Override
            public void doTask(Integer id) {

            }
        };
        // 2.匿名内部类
        Task taskTestInstance1 = new MyTask() {
            @Override
            public void doTask(Integer id) {
                super.doTask(id);
                System.out.println("instance" + id);
            }
        };
        taskTestInstance1.doTask(1);
//        Task taskTestInstance2 = (Integer id)->{
//            System.out.println("instance2");
//        };
        //3.Lambda表达式
        Task taskTestInstance3 = id -> System.out.println("instance" + id);
        taskTestInstance3.doTask(3);
        // 4.方法引用
        MyTask mt = new MyTask();
        Task taskTestInstance4 = mt::doTask;
//        Task taskTestInstance3 = new MyTask() {};
//        User::new    NOTE:本质上就是重写，只要保证两同两小一大原则就可以
        taskTestInstance4.doTask(4);
    }
}
