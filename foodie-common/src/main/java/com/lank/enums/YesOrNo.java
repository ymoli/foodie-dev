package com.lank.enums;

//是否枚举
public enum YesOrNo {
    Yes(1,"是"),
    No(0,"否");

    public final Integer type;
    public final String desc;

    YesOrNo(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
