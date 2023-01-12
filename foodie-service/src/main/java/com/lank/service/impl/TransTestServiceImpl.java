package com.lank.service.impl;

import com.lank.mapper.StuMapper;
import com.lank.pojo.Stu;
import com.lank.service.TransTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransTestServiceImpl implements TransTestService {

    @Autowired
    private StuMapper stuMapper;

//    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void transTest() {
        saveParetn();
//        int a = 1/0;
        saveChildren();
    }
    public void saveParetn() {
        Stu stu = new Stu();
        stu.setName("parent");
        stu.setAge(19);
        stuMapper.insert(stu);
    }
//    @Transactional(propagation = Propagation.REQUIRED)
    public void saveChildren() {
        saveChild1();
//        int a = 1/0;
        saveChild2();
    }
    public void saveChild1() {
        Stu stu = new Stu();
        stu.setName("chid-1");
        stu.setAge(19);
        stuMapper.insert(stu);
    }
    public void saveChild2() {
        Stu stu = new Stu();
        stu.setName("chid-2");
        stu.setAge(19);
        stuMapper.insert(stu);
    }
}
