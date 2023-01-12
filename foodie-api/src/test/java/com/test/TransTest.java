package com.test;

import com.lank.Application;
import com.lank.service.StuService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TransTest {
    @Autowired
    private StuService transTestService;

    @Test
    public void myTest(){
//        transTestService.transTest();
        transTestService.saveStu();
    }
}
