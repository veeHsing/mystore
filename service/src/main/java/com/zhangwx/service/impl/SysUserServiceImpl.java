package com.zhangwx.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhangwx.constants.MyExceptionCode;
import com.zhangwx.dao.SysResourcesMapper;
import com.zhangwx.dao.SysRoleMapper;
import com.zhangwx.dao.SysRoleResourcesMapper;
import com.zhangwx.dao.SysUserMapper;
import com.zhangwx.enums.EnumSysResources;
import com.zhangwx.enums.EnumSysRoleResources;
import com.zhangwx.exception.ServiceException;
import com.zhangwx.input.LoginInput;
import com.zhangwx.input.SimplePageInput;
import com.zhangwx.input.SysUserListInput;
import com.zhangwx.model.*;
import com.zhangwx.output.MenuOutput;
import com.zhangwx.output.SysResourcesTree;
import com.zhangwx.output.SysUserInfoOutput;
import com.zhangwx.output.SysUserListOutput;
import com.zhangwx.service.SysUserService;
import com.zhangwx.service.TokenService;
import com.zhangwx.util.MD5Util;
import com.zhangwx.util.UserRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired(required = false)
    private SysUserMapper sysUserMapper;
    @Autowired
    private ServiceException serviceException;
    @Autowired
    private TokenService tokenService;

    @Autowired(required = false)
    private SysRoleMapper sysRoleMapper;

    @Autowired(required = false)
    private SysRoleResourcesMapper sysRoleResourcesMapper;

    @Autowired(required = false)
    private SysResourcesMapper sysResourcesMapper;

    @Override
    public SysUser selectByPrimaryKey(Long id) {
           return sysUserMapper.selectByPrimaryKey(id);
    }

    @Override
    public String login(LoginInput loginInput) {
        Optional.ofNullable(loginInput).orElseThrow(()->new ServiceException("登录失败！"));
        SysUserExample example=new SysUserExample();
        SysUserExample.Criteria criteria=example.createCriteria();
        criteria.andUserNameEqualTo(loginInput.getUsername());
        List<SysUser> sysUserList=sysUserMapper.selectByExample(example);
        if (sysUserList.size() == 0){
            throw new ServiceException(MyExceptionCode.SYS_USER_NOT_EXIST);
        }
        SysUser user=sysUserList.get(0);
        if (!MD5Util.getMD5(loginInput.getPassword()).equals(user.getPassword())){
            throw new ServiceException(MyExceptionCode.SYS_USER_PASSWORD_ERROR);
        }
        return tokenService.createToken(user);
    }

    @Override
    public SysUser selectByUsername(String username) {
        SysUserExample example=new SysUserExample();
        SysUserExample.Criteria criteria=example.createCriteria();
        criteria.andUserNameEqualTo(username);
        List<SysUser> sysUserList=sysUserMapper.selectByExample(example);
        if (sysUserList.size() == 0){
            return  null;
        }
        return sysUserList.get(0);
    }

    @Override
    public SysRole selectSysRoleByPrimaryKey(Long id) {
        return  sysRoleMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<SysRoleResouresMap> getFilterChainDefinitionMap() {
        return sysRoleResourcesMapper.getFilterChainDefinitionMap();
    }

    @Override
    public SysResources selectSysResourcesByCode(String code) {
        SysResourcesExample example=new SysResourcesExample();
        SysResourcesExample.Criteria criteria=example.createCriteria();
//        criteria.andCodeEqualTo(code);
        List<SysResources> list=sysResourcesMapper.selectByExample(example);
        if (list.size() == 0){
            return  null;
        }
        return list.get(0);
    }

    @Override
    public SysUserInfoOutput getInfo() {
//        Long userId= UserRequest.getCurrentUserId();
        Long userId=1L;
        SysUser sysUser=sysUserMapper.selectByPrimaryKey(userId);
        Optional.ofNullable(sysUser).orElseThrow(()->new ServiceException(MyExceptionCode.SYS_USER_NOT_EXIST));
        SysRole sysRole=sysRoleMapper.selectByPrimaryKey(sysUser.getRoleId());
        SysUserInfoOutput sysUserInfoOutput=new SysUserInfoOutput();
        sysUserInfoOutput.setName(sysUser.getUserName());
        sysUserInfoOutput.setAvatar(sysUser.getAvatarImg());
        if (sysRole.getCode() != null || sysRole.getCode() != ""){
            List<String> list=new ArrayList<String>();
            list.add(sysRole.getCode());
            sysUserInfoOutput.setRoles(list);
        }
        SysRoleResourcesExample example=new SysRoleResourcesExample();
        SysRoleResourcesExample.Criteria criteria=example.createCriteria();
        criteria.andRoleIdEqualTo(sysUser.getRoleId());
        criteria.andDeletedEqualTo((byte)EnumSysRoleResources.DELETED_NO.getCode());
        List<SysRoleResources> roleResourcesList=sysRoleResourcesMapper.selectBySimpleExample(example);
        List<SysResources> resourcesList=new ArrayList<>();
        for (SysRoleResources value:roleResourcesList ){
            resourcesList.add(value.getResources());
        }
        sysUserInfoOutput.setSysResources(resourcesList);
        return sysUserInfoOutput;
    }

    private void resTree(List<SysResources> list){
        //后台显示菜单默认为2层
        //1层是目录，2层为功能路由
        List<MenuOutput> menuOutputList=new ArrayList<>();
        for (SysResources resources : list){
            MenuOutput menu=new MenuOutput();
            menu.setPath(resources.getPath());
            menu.setRedirect(resources.getRedirect());
            menu.setName(resources.getName());
            if (resources.getType() == EnumSysResources.TYPE_DIR.getCode()){
                //资源为目录



            }else if(resources.getType() == EnumSysResources.TYPE_MENU.getCode() && resources.getParentId() == 0){

            }else {

            }



        }

    }

    @Override
    public PageInfo<SysUserListOutput> getSysUserList(SysUserListInput sysUserListInput) {
        PageHelper.startPage(sysUserListInput.getPage(),sysUserListInput.getLimit());
        SysUserExample example=new SysUserExample();
        SysUserExample.Criteria criteria=example.createCriteria();
        criteria.andDeletedEqualTo((byte)0);
        List<SysUserListOutput> list=sysUserMapper.selectListByExample(example);
        PageInfo<SysUserListOutput> pageInfo=new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public PageInfo<SysRole> getSysRolesList(SimplePageInput simplePageInput) {
        PageHelper.startPage(simplePageInput.getPage(),simplePageInput.getLimit());
        SysRoleExample example=new SysRoleExample();
        SysRoleExample.Criteria criteria=example.createCriteria();
        criteria.andDeletedEqualTo((byte)0);
        List<SysRole> list=sysRoleMapper.selectByExample(example);
        PageInfo<SysRole> pageInfo=new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public List<SysResourcesTree> getSysResourcesList() {
        SysResourcesExample example=new SysResourcesExample();
        SysResourcesExample.Criteria criteria=example.createCriteria();
        criteria.andDeletedEqualTo(0);
        List<SysResources> list=sysResourcesMapper.selectByExample(example);
        List<SysResourcesTree> result=new ArrayList<>();
        for (SysResources resources : list){
            SysResourcesTree tree=new SysResourcesTree();
            BeanUtils.copyProperties(resources,tree);
            result.add(tree);
        }
        return listToTree(result);
    }

    private  List<SysResourcesTree> listToTree(List<SysResourcesTree> list) {
        //用递归找子。
        List<SysResourcesTree> treeList = new ArrayList<SysResourcesTree>();
        for (SysResourcesTree tree : list) {
            if (tree.getParentId() == 0) {
                treeList.add(findChildren(tree, list));
            }
        }
        return treeList;
    }

    private  SysResourcesTree findChildren(SysResourcesTree tree, List<SysResourcesTree> list) {
        for (SysResourcesTree node : list) {
            if (node.getParentId() == tree.getId()) {
                if (tree.getChildren() == null) {
                    tree.setChildren(new ArrayList<>());
                }
                tree.getChildren().add(findChildren(node, list));
            }
        }
        return tree;
    }


    @Override
    public Boolean addSysResource(SysResources sysResources) {
        Optional.ofNullable(sysResources).orElseThrow(()->new ServiceException(MyExceptionCode.SYS_HTTP_MESSAGE));
        sysResources.setDeleted(EnumSysResources.DELETE_NO.getCode());
        sysResources.setCreateBy((int)UserRequest.getCurrentUserId());
        sysResources.setCreateAt(new Date());
        sysResources.setUpdateAt(new Date());
        sysResources.setHideinmenu(EnumSysResources.HIDDEN_NO.getCode());
        int bool = sysResourcesMapper.insert(sysResources);
        if (bool >= 1){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public Boolean deleteResource(SysResources sysResources) {
        Optional.ofNullable(sysResources).orElseThrow(()->new ServiceException(MyExceptionCode.SYS_HTTP_MESSAGE));
        SysResources sysResources1=sysResourcesMapper.selectByPrimaryKey(sysResources.getId());
        Optional.ofNullable(sysResources1).orElseThrow(()->new ServiceException(MyExceptionCode.SYS_RESOURCE_NOT_EXITS));
        if (sysResources1.getParentId() > 0){
            throw new ServiceException(MyExceptionCode.SYS_CANT_DELETE);
        }
        SysResourcesExample example=new SysResourcesExample();
        SysResourcesExample.Criteria criteria=example.createCriteria();
        criteria.andIdEqualTo(sysResources.getId());
        int del=sysResourcesMapper.deleteByExample(example);
        if (del > 0){
            return  true;
        }else {
            return  false;
        }
    }
}
