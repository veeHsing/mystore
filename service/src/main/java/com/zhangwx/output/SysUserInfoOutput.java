package com.zhangwx.output;

import com.zhangwx.model.SysResources;

import java.util.ArrayList;
import java.util.List;

public class SysUserInfoOutput {

    private List roles;//角色
    private String name;//用户名
    private String avatar;//头像
    private String introduction;//介绍
    private List<SysResources> sysResources;//角色对应的资源

    public List getRoles() {
        return roles;
    }

    public void setRoles(List roles) {
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public List<SysResources> getSysResources() {
        return sysResources;
    }

    public void setSysResources(List<SysResources> sysResources) {
        this.sysResources = sysResources;
    }
}
