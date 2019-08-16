package com.zhangwx.service;

import com.github.pagehelper.PageInfo;
import com.zhangwx.input.LoginInput;
import com.zhangwx.input.SimplePageInput;
import com.zhangwx.input.SysUserListInput;
import com.zhangwx.model.SysResources;
import com.zhangwx.model.SysRole;
import com.zhangwx.model.SysRoleResouresMap;
import com.zhangwx.model.SysUser;
import com.zhangwx.output.SysResourcesTree;
import com.zhangwx.output.SysUserInfoOutput;
import com.zhangwx.output.SysUserListOutput;

import java.util.List;

public interface SysUserService {


    SysUser selectByPrimaryKey(Long id);

    String login(LoginInput loginInput);

    SysUser selectByUsername(String username);

    SysRole selectSysRoleByPrimaryKey(Long id);

    /**
     * 权限角色对应表，
     * 授权根据此判断角色是否有权限访问接口
     * @return
     */
    List<SysRoleResouresMap> getFilterChainDefinitionMap();

    SysResources selectSysResourcesByCode(String code);

    /**
     * 获取系统用户信息
     * @return
     */
    SysUserInfoOutput getInfo();

    PageInfo<SysUserListOutput> getSysUserList(SysUserListInput sysUserListInput);

    PageInfo<SysRole> getSysRolesList(SimplePageInput simplePageInput);

    //获取资源列表
    List<SysResourcesTree> getSysResourcesList();

    // 添加资源
    Boolean addSysResource(SysResources sysResources);
    //删除资源
    Boolean deleteResource(SysResources sysResources);


}