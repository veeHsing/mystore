package com.zhangwx.enums;

public enum EnumSysUser {
    disableStatus(0,"冻结"),
    enableStatus(1,"正常"),
    ;

    private int status;
    private String msg;

    EnumSysUser(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
