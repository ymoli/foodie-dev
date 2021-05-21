package com.lank.enums;

//设置性别枚举
public enum Sex {
    woman(0,"女"),
    man(1,"男"),
    secret(2,"保密");

    public final Integer type;
    public final String desc;

    Sex(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
