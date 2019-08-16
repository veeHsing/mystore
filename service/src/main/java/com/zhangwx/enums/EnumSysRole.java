package com.zhangwx.enums;

public enum EnumSysRole {
    DELETED_NO(1,"已删除"),
    DELETED_YES(0,"正常"),

    ;

    private int code;
    private String msg;

    EnumSysRole(int code, String msg) {
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
