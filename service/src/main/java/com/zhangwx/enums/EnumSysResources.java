package com.zhangwx.enums;

public enum EnumSysResources {
    TYPE_DIR(0,"目录"),
    TYPE_MENU(1,"菜单"),
    TYPE_BUTTON(2,"按钮"),
    DELETE_YES(1,"已删除"),
    DELETE_NO(0,"正常"),
    HIDDEN_YES(1,"隐藏"),
    HIDDEN_NO(0,"正常")

    ;

    private int code;
    private String msg;

    EnumSysResources(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
