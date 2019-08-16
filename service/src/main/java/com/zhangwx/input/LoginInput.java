package com.zhangwx.input;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class LoginInput {

    @NotBlank(message = "用户名不能为空！")
    private String username;
    @NotBlank(message = "密码不能为空！")
    @Size(min = 6,message = "密码不能少于6位！")
    private String password;
    private Integer rememberMe;

    public LoginInput() {
    }

    public LoginInput(String username, String password, Integer rememberMe) {
        this.username = username;
        this.password = password;
        this.rememberMe = rememberMe;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(Integer rememberMe) {
        this.rememberMe = rememberMe;
    }
}
