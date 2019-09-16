package com.zhangwx.controller;

import com.github.pagehelper.PageInfo;
import com.zhangwx.base.Result;
import com.zhangwx.constants.MyExceptionCode;
import com.zhangwx.input.LoginInput;
import com.zhangwx.input.SimplePageInput;
import com.zhangwx.input.SysUserListInput;
import com.zhangwx.model.SysResources;
import com.zhangwx.model.SysRole;
import com.zhangwx.model.SysUser;
import com.zhangwx.output.MenuOutput;
import com.zhangwx.output.SysResourcesTree;
import com.zhangwx.output.SysUserInfoOutput;
import com.zhangwx.output.SysUserListOutput;
import com.zhangwx.service.SysUserService;
import com.zhangwx.shiro.MyFilterChainDefinitions;
import com.zhangwx.util.ResultsUtil;
import com.zhangwx.util.UserRequest;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/system")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/user")
    public Result findOne(HttpServletRequest request) {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        System.out.println(session);
        return ResultsUtil.success(sysUserService.selectByPrimaryKey(1l));
    }

    /**
     * 登录获取认证
     *
     * @param loginInput
     * @return
     */
    @RequestMapping("/login")
    public Result login(@Validated @RequestBody LoginInput loginInput) {
        System.out.println(loginInput);
        String token = sysUserService.login(loginInput);
        if (token != null) {
            Map t = new HashMap();
            t.put("access_token", token);
            return ResultsUtil.success("登录成功!", t);
        } else {
            return ResultsUtil.failure("登录失败!");
        }
    }

    @RequestMapping("/logout")
    public Result logout() {
        return ResultsUtil.success();
    }

    @RequestMapping("/info")
    public Result getUserInfo() {
        SysUserInfoOutput output = sysUserService.getInfo();
//        logger.info(sysUser.getUserName());
        if (output != null) {
            return ResultsUtil.success(output);
        }
        return ResultsUtil.failure(MyExceptionCode.SYS_USER_NOT_EXIST);
    }

    @RequestMapping("/user/list")
    public Result getSysUserList(@RequestBody SysUserListInput sysUserListInput) {
        PageInfo<SysUserListOutput> pageInfo = sysUserService.getSysUserList(sysUserListInput);
        return ResultsUtil.success(pageInfo);
    }

    @RequestMapping("/user/add")
    public Result addUser(@RequestBody SysUser sysUser) {
        boolean res = sysUserService.addUser(sysUser);
        if (res){
            return ResultsUtil.success();
        }
       return ResultsUtil.failure();
    }

    @RequestMapping("/user/edit")
    public Result updateUser(@RequestBody SysUser sysUser) {
        SysUserListOutput sysUserListOutput = sysUserService.updateUser(sysUser);
        return ResultsUtil.success(sysUserListOutput);
    }

    @RequestMapping(value = "/user/delete/{id}",method = RequestMethod.DELETE)
    public Result deleteUser(@PathVariable long id) {
        int res = sysUserService.deleteUser(id);
        if (res > 0){
            return ResultsUtil.success();
        }
        return ResultsUtil.failure();
    }

    @RequestMapping("/role/list")
    public Result getSysRolesList(@RequestBody SimplePageInput sysUserListInput) {
        PageInfo<SysRole> pageInfo = sysUserService.getSysRolesList(sysUserListInput);
        return ResultsUtil.success(pageInfo);
    }

    @RequestMapping("/role/add")
    public Result addRole(@Validated(value = {SysRole.add.class}) @RequestBody SysRole sysRole) {
        int res = sysUserService.addRole(sysRole);
        if (res > 0){
            return ResultsUtil.success();
        }
        return ResultsUtil.failure();
    }

    @RequestMapping("/role/update")
    public Result updateRole(@Validated(value = {SysRole.update.class}) @RequestBody SysRole sysRole) {
        int res  = sysUserService.updateRole(sysRole);
        if (res > 0){
            return ResultsUtil.success();
        }
        return ResultsUtil.failure();
    }

    @RequestMapping("/role/delete/{id}")
    public Result deleteRole(@PathVariable long id) {
        int res = sysUserService.deleteRole(id);
        if (res > 0){
            return ResultsUtil.success();
        }
        return ResultsUtil.failure();
    }

    @RequestMapping("/roles")
    public Result getSysRoles() {
        List<SysRole> roles = sysUserService.getSysRoles();
        return ResultsUtil.success(roles);
    }

    //资源列表调用
    @RequestMapping("/resources/list")
    public Result getSysResourcesList() {
        long userId = UserRequest.getCurrentUserId();
        List<SysResourcesTree> list = sysUserService.getSysResourcesList();
        return ResultsUtil.success(list);
    }

    //权限分配调用
    @RequestMapping("/resources/list2")
    public Result getSysResourcesList2(@RequestParam Integer roleId) {
        List<SysResourcesTree> list = sysUserService.getSysResourcesList();
        return ResultsUtil.success(list);
    }

    @RequestMapping("/resources/add")
    public Result addSysResource(@Validated @RequestBody SysResources sysResources) {
        boolean result = sysUserService.addSysResource(sysResources);
        if (result) {
            return ResultsUtil.success();
        } else {
            return ResultsUtil.success();
        }
    }

    @RequestMapping("/resources/edit")
    public Result editSysResource(@Validated @RequestBody SysResources sysResources) {
        boolean result = sysUserService.updateSysResource(sysResources);
        if (result) {
            return ResultsUtil.success();
        } else {
            return ResultsUtil.success();
        }
    }

    @RequestMapping("/resources/delete")
    public Result deleteSysResource(@RequestBody SysResources sysResources) {
        boolean result = sysUserService.deleteResource(sysResources);
        if (result) {
            return ResultsUtil.success();
        } else {
            return ResultsUtil.success();
        }
    }
    @Autowired
    private MyFilterChainDefinitions myFilterChainDefinitions;
    @Autowired
    private  ShiroFilterFactoryBean shiroFilterFactoryBean;
    //权限分配
    @RequestMapping("/permission/assign")
    public Result assignPermission(@RequestBody Map map) {
        sysUserService.assignPermission(map);
        myFilterChainDefinitions.updatePermission(shiroFilterFactoryBean);
        return ResultsUtil.success();
    }

    //根据角色id获取权限
    @RequestMapping("/permission/checked_keys")
    public Result getPermissionByRole(@RequestBody Map map) {
        List<Integer> list = sysUserService.getPermissionByRole((int) map.get("roleId"));
        return ResultsUtil.success(list);
    }

    //前端路由
    @RequestMapping("/permission/getAsyncRoutes")
    public Result getAsyncRoutes(){
        List<MenuOutput> list=sysUserService.getAsyncRoutes();
        return ResultsUtil.success(list);
    }

}
