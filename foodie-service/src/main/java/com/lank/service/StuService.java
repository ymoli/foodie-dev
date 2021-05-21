package com.lank.service;

import com.lank.pojo.Stu;

public interface StuService {
    public Stu getStu(int id);
    public void updateStu(int id);
    public void saveStu();
    public void deleteStu(int id);
}
