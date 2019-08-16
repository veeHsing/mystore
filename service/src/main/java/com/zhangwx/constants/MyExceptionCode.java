package com.zhangwx.constants;

/**
 * 全局错误码
 */
public enum MyExceptionCode {

    SYS_RESULT_SUCCESS(0,"成功"),
    SYS_EXCEPTION(-1,"系统异常"),
    SYS_HTTP_MESSAGE(-2,"参数异常"),
    SYS_CANT_DELETE(-3,"不能删除"),

    //sys_user
    SYS_USER_NOT_EXIST(-1000,"用户不存在！"),
    SYS_USER_PASSWORD_ERROR(-1001,"密码错误！"),
    SYS_DENY(-1003,"你没有权限"),
    SYS_USER_STATUS_EXCEPTION(-1004,"用户状态异常"),
    SYS_ROLE_NOT_EXIST(-1005,"角色不存在"),

    SYS_JWT_CREATE_ERROR(-1100,"jwt创建失败"),
    SYS_JWT_PARSE_FAIL(-1101,"token无效"),

    SYS_RESOURCE_NOT_EXITS(-1200,"资源不存在"),



    ;

    MyExceptionCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Integer code;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
