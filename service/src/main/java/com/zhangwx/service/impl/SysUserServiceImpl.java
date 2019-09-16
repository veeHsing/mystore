package com.zhangwx.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhangwx.constants.MyExceptionCode;
import com.zhangwx.dao.SysResourcesMapper;
import com.zhangwx.dao.SysRoleMapper;
import com.zhangwx.dao.SysRoleResourcesMapper;
import com.zhangwx.dao.SysUserMapper;
import com.zhangwx.enums.EnumSysResources;
import com.zhangwx.enums.EnumSysRole;
import com.zhangwx.enums.EnumSysRoleResources;
import com.zhangwx.enums.EnumSysUser;
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
import org.apache.catalina.User;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

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

    private static final Logger logger = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Override
    public SysUser selectByPrimaryKey(Long id) {
        return sysUserMapper.selectByPrimaryKey(id);
    }

    @Override
    public String login(LoginInput loginInput) {
        Optional.ofNullable(loginInput).orElseThrow(() -> new ServiceException("登录失败！"));
        SysUserExample example = new SysUserExample();
        SysUserExample.Criteria criteria = example.createCriteria();
        criteria.andUserNameEqualTo(loginInput.getUsername());
        List<SysUser> sysUserList = sysUserMapper.selectByExample(example);
        if (sysUserList.size() == 0) {
            throw new ServiceException(MyExceptionCode.SYS_USER_NOT_EXIST);
        }
        SysUser user = sysUserList.get(0);
        if (!MD5Util.getMD5(loginInput.getPassword()).equals(user.getPassword())) {
            throw new ServiceException(MyExceptionCode.SYS_USER_PASSWORD_ERROR);
        }
        return tokenService.createToken(user);
    }

    @Override
    public SysUser selectByUsername(String username) {
        SysUserExample example = new SysUserExample();
        SysUserExample.Criteria criteria = example.createCriteria();
        criteria.andUserNameEqualTo(username);
        List<SysUser> sysUserList = sysUserMapper.selectByExample(example);
        if (sysUserList.size() == 0) {
            return null;
        }
        return sysUserList.get(0);
    }

    @Override
    public SysRole selectSysRoleByPrimaryKey(Long id) {
        return sysRoleMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<SysRoleResouresMap> getFilterChainDefinitionMap() {
        return sysRoleResourcesMapper.getFilterChainDefinitionMap();
    }

    @Override
    public SysResources selectSysResourcesByCode(String code) {
        SysResourcesExample example = new SysResourcesExample();
        SysResourcesExample.Criteria criteria = example.createCriteria();
//        criteria.andCodeEqualTo(code);
        List<SysResources> list = sysResourcesMapper.selectByExample(example);
        if (list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public SysUserInfoOutput getInfo() {
//        Long userId= UserRequest.getCurrentUserId();
        Long userId = 1L;
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(userId);
        Optional.ofNullable(sysUser).orElseThrow(() -> new ServiceException(MyExceptionCode.SYS_USER_NOT_EXIST));
        SysRole sysRole = sysRoleMapper.selectByPrimaryKey(sysUser.getRoleId());
        SysUserInfoOutput sysUserInfoOutput = new SysUserInfoOutput();
        sysUserInfoOutput.setName(sysUser.getUserName());
        sysUserInfoOutput.setAvatar(sysUser.getAvatarImg());
        if (sysRole.getCode() != null || sysRole.getCode() != "") {
            List<String> list = new ArrayList<String>();
            list.add(sysRole.getCode());
            sysUserInfoOutput.setRoles(list);
        }
        SysRoleResourcesExample example = new SysRoleResourcesExample();
        SysRoleResourcesExample.Criteria criteria = example.createCriteria();
        criteria.andRoleIdEqualTo(sysUser.getRoleId());
        criteria.andDeletedEqualTo((byte) EnumSysRoleResources.DELETED_NO.getCode());
        List<SysRoleResources> roleResourcesList = sysRoleResourcesMapper.selectBySimpleExample(example);
        List<SysResources> resourcesList = new ArrayList<>();
        for (SysRoleResources value : roleResourcesList) {
            resourcesList.add(value.getResources());
        }
        sysUserInfoOutput.setSysResources(resourcesList);
        return sysUserInfoOutput;
    }

    private void resTree(List<SysResources> list) {
        //后台显示菜单默认为2层
        //1层是目录，2层为功能路由
        List<MenuOutput> menuOutputList = new ArrayList<>();
        for (SysResources resources : list) {
            MenuOutput menu = new MenuOutput();
            menu.setRedirect(resources.getRedirect());
            menu.setName(resources.getName());
            if (resources.getType() == EnumSysResources.TYPE_DIR.getCode()) {
                //资源为目录


            } else if (resources.getType() == EnumSysResources.TYPE_MENU.getCode() && resources.getParentId() == 0) {

            } else {

            }


        }

    }

    @Override
    public PageInfo<SysUserListOutput> getSysUserList(SysUserListInput sysUserListInput) {
        PageHelper.startPage(sysUserListInput.getPage(), sysUserListInput.getLimit());
        SysUserExample example = new SysUserExample();
        SysUserExample.Criteria criteria = example.createCriteria();
        criteria.andDeletedEqualTo((byte) 0);
        List<SysUserListOutput> list = sysUserMapper.selectListByExample(example);
        PageInfo<SysUserListOutput> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public PageInfo<SysRole> getSysRolesList(SimplePageInput simplePageInput) {
        PageHelper.startPage(simplePageInput.getPage(), simplePageInput.getLimit());
        SysRoleExample example = new SysRoleExample();
        SysRoleExample.Criteria criteria = example.createCriteria();
        criteria.andDeletedEqualTo((byte) 0);
        List<SysRole> list = sysRoleMapper.selectByExample(example);
        PageInfo<SysRole> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public int addRole(SysRole sysRole) {
        Optional.ofNullable(sysRole).orElseThrow(() -> new ServiceException(MyExceptionCode.SYS_HTTP_MESSAGE));
        sysRole.setCreateAt(new Date());
        sysRole.setCreateBy(UserRequest.getCurrentUserId());
        sysRole.setDeleted((byte) EnumSysRole.DELETED_NO.getCode());
        return sysRoleMapper.insertSelective(sysRole);
    }

    @Override
    public int updateRole(SysRole sysRole) {
        Optional.ofNullable(sysRole).orElseThrow(() -> new ServiceException(MyExceptionCode.SYS_HTTP_MESSAGE));
        SysRole sysRoleOld = sysRoleMapper.selectByPrimaryKey(sysRole.getId());
        Optional.ofNullable(sysRoleOld).orElseThrow(() -> new ServiceException(MyExceptionCode.SYS_ROLE_NOT_EXIST));
        sysRole.setUpdateAt(new Date());
        sysRole.setUpdateBy(UserRequest.getCurrentUserId());
        return sysRoleMapper.updateByPrimaryKeySelective(sysRole);
    }

    @Override
    public int deleteRole(long id) {
        SysRole sysRoleOld = sysRoleMapper.selectByPrimaryKey(id);
        Optional.ofNullable(sysRoleOld).orElseThrow(() -> new ServiceException(MyExceptionCode.SYS_ROLE_NOT_EXIST));
        sysRoleOld.setUpdateAt(new Date());
        sysRoleOld.setUpdateBy(UserRequest.getCurrentUserId());
        sysRoleOld.setDeleted((byte)EnumSysRole.DELETED_YES.getCode());
        return sysRoleMapper.updateByPrimaryKeySelective(sysRoleOld);
    }

    @Override
    public List<SysRole> getSysRoles() {
        SysRoleExample example = new SysRoleExample();
        SysRoleExample.Criteria criteria = example.createCriteria();
        criteria.andDeletedEqualTo((byte) EnumSysRole.DELETED_NO.getCode());
        List<SysRole> list = sysRoleMapper.selectByExample(example);
        return list;
    }

    @Override
    public List<SysResourcesTree> getSysResourcesList() {
        SysResourcesExample example = new SysResourcesExample();
        SysResourcesExample.Criteria criteria = example.createCriteria();
        criteria.andDeletedEqualTo(0);
        List<SysResources> list = sysResourcesMapper.selectByExample(example);
        List<SysResourcesTree> result = new ArrayList<>();
        for (SysResources resources : list) {
            SysResourcesTree tree = new SysResourcesTree();
            BeanUtils.copyProperties(resources, tree);
            result.add(tree);
        }
        return listToTree(result);
    }

    private List<SysResourcesTree> listToTree(List<SysResourcesTree> list) {
        //用递归找子。
        List<SysResourcesTree> treeList = new ArrayList<SysResourcesTree>();
        for (SysResourcesTree tree : list) {
            if (tree.getParentId() == 0) {
                treeList.add(findChildren(tree, list));
            }
        }
        return treeList;
    }

    private SysResourcesTree findChildren(SysResourcesTree tree, List<SysResourcesTree> list) {
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
    public boolean addSysResource(SysResources sysResources) {
        Optional.ofNullable(sysResources).orElseThrow(() -> new ServiceException(MyExceptionCode.SYS_HTTP_MESSAGE));
        sysResources.setDeleted(EnumSysResources.DELETE_NO.getCode());
        sysResources.setCreateBy((int) UserRequest.getCurrentUserId());
        sysResources.setCreateAt(new Date());
        sysResources.setUpdateAt(new Date());
        if (sysResources.getType() == EnumSysResources.TYPE_BUTTON.getCode()) {
            sysResources.setHide(EnumSysResources.HIDDEN_YES.getCode());
        } else {
            sysResources.setHide(EnumSysResources.HIDDEN_NO.getCode());
        }
        int bool = sysResourcesMapper.insert(sysResources);
        if (bool >= 1) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean updateSysResource(SysResources sysResources) {
        Optional.ofNullable(sysResources).orElseThrow(() -> new ServiceException(MyExceptionCode.SYS_HTTP_MESSAGE));
        int bool = sysResourcesMapper.updateByPrimaryKey(sysResources);
        if (bool >= 1) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean deleteResource(SysResources sysResources) {
        Optional.ofNullable(sysResources).orElseThrow(() -> new ServiceException(MyExceptionCode.SYS_HTTP_MESSAGE));
        SysResources sysResourcesOrg = sysResourcesMapper.selectByPrimaryKey(sysResources.getId());
        Optional.ofNullable(sysResourcesOrg).orElseThrow(() -> new ServiceException(MyExceptionCode.SYS_RESOURCE_NOT_EXITS));
        //判断当前菜单是否有孩子
        SysResourcesExample exampleChild = new SysResourcesExample();
        SysResourcesExample.Criteria criteriaChild = exampleChild.createCriteria();
        criteriaChild.andParentIdEqualTo(sysResourcesOrg.getId());
        criteriaChild.andDeletedEqualTo(EnumSysResources.DELETE_NO.getCode());
        List<SysResources> sysResourcesChildList = sysResourcesMapper.selectByExample(exampleChild);
        if (sysResourcesChildList.size() > 0) {
            throw new ServiceException(MyExceptionCode.SYS_RESOURCE_HAS_CHILD);
        }
        SysResourcesExample example = new SysResourcesExample();
        SysResourcesExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(sysResources.getId());
        sysResourcesMapper.deleteByExample(example);

        SysRoleResourcesExample sysRoleResourcesExample = new SysRoleResourcesExample();
        SysRoleResourcesExample.Criteria criteria1 = sysRoleResourcesExample.createCriteria();
        criteria1.andResourceIdEqualTo(sysResources.getId().longValue());
        sysRoleResourcesMapper.deleteByExample(sysRoleResourcesExample);
        return true;
    }

    @Override
    public boolean addUser(SysUser sysUser) {
        Optional.ofNullable(sysUser).orElseThrow(() -> new ServiceException(MyExceptionCode.SYS_HTTP_MESSAGE));
        if (!StringUtils.isEmpty(sysUser.getPassword())) {
            sysUser.setPassword(MD5Util.getMD5(sysUser.getPassword()));
        }
        sysUser.setStat((byte) EnumSysUser.enableStatus.getStatus());
        sysUser.setDeleted((byte) EnumSysUser.DELETED_NO.getStatus());
        sysUser.setCreateAt(new Date());
        sysUser.setCreateBy(UserRequest.getCurrentUserId());
        int res = sysUserMapper.insertSelective(sysUser);
        if (res > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public SysUserListOutput updateUser(SysUser sysUser) {
        Optional.ofNullable(sysUser).orElseThrow(() -> new ServiceException(MyExceptionCode.SYS_HTTP_MESSAGE));
        SysUser sysUserOld = sysUserMapper.selectByPrimaryKey(sysUser.getId());
        Optional.ofNullable(sysUserOld).orElseThrow(() -> new ServiceException(MyExceptionCode.SYS_USER_NOT_EXIST));
        if (!StringUtils.isEmpty(sysUser.getPassword())) {
            sysUser.setPassword(MD5Util.getMD5(sysUser.getPassword()));
        }
        sysUser.setUpdateAt(new Date());
        sysUser.setUpdateBy(UserRequest.getCurrentUserId());
        int bool = sysUserMapper.updateByPrimaryKeySelective(sysUser);
        logger.info("=========+" + bool);
        if (bool >= 1) {
            SysRole sysRole = sysRoleMapper.selectByPrimaryKey(sysUser.getRoleId());
            SysUserListOutput sysUserListOutput = new SysUserListOutput();
            BeanUtils.copyProperties(sysUserOld, sysUserListOutput);
            sysUserListOutput.setRoleName(sysRole.getName());
            return sysUserListOutput;
        } else {
            return null;
        }
    }

    @Override
    public int deleteUser(long id) {
        SysUser sysUserOld = sysUserMapper.selectByPrimaryKey(id);
        Optional.ofNullable(sysUserOld).orElseThrow(() -> new ServiceException(MyExceptionCode.SYS_USER_NOT_EXIST));
        sysUserOld.setDeleted((byte) EnumSysUser.DELETED_YES.getStatus());
        return sysUserMapper.updateByPrimaryKey(sysUserOld);
    }

    @Override
    @Transactional
    public boolean assignPermission(Map map) {
        int roleId = (int) map.get("roleId");
        List<Integer> keys = (List<Integer>) map.get("keys");
        if (roleId == 0 || keys.size() == 0) {
            throw new ServiceException("没有选择角色或没有勾选权限");
        }
        SysRoleResourcesExample example = new SysRoleResourcesExample();
        SysRoleResourcesExample.Criteria criteria = example.createCriteria();
        criteria.andRoleIdEqualTo((long) roleId);
        sysRoleResourcesMapper.deleteByExample(example);
        for (int resourceId : keys) {
            SysRoleResources sysRoleResources = new SysRoleResources();
            sysRoleResources.setRoleId((long) roleId);
            sysRoleResources.setResourceId((long) resourceId);
            sysRoleResources.setDeleted((byte) 0);
            sysRoleResources.setCreateAt(new Date());
            sysRoleResources.setCreateBy(UserRequest.getCurrentUserId());
            SysResources sysResources = sysResourcesMapper.selectByPrimaryKey(resourceId);
            sysRoleResources.setParentId(sysResources.getParentId().longValue());
            sysRoleResourcesMapper.insertSelective(sysRoleResources);
        }
        return true;
    }

    @Override
    public List<Integer> getPermissionByRole(int roleId) {
        SysRoleResourcesExample example = new SysRoleResourcesExample();
        SysRoleResourcesExample.Criteria criteria = example.createCriteria();
        criteria.andRoleIdEqualTo((long) roleId);
        List<SysRoleResources> list = sysRoleResourcesMapper.selectByExample(example);
        List<Integer> checkKeys = new ArrayList<>();
        for (SysRoleResources sysRoleResources : list) {
            if (StringUtils.isEmpty(sysRoleResources.getResourceId())) {
                continue;
            }
            checkKeys.add(sysRoleResources.getResourceId().intValue());
        }
        return checkKeys;
    }

    @Override
    public List<MenuOutput> getAsyncRoutes() {

        //1遍历菜单
        List<SysResourcesTree> treeList = getSysResourcesList();
        List<MenuOutput> finalList = new ArrayList<>();
        for (SysResourcesTree o : treeList) {
            MenuOutput dir = new MenuOutput();
            dir.setPath("/" + o.getName());
            dir.setName(o.getName());
            MenuOutput.Meta metaDir = new MenuOutput().new Meta();
            metaDir.setTitle(o.getTitle());
            metaDir.setIcon(o.getIcon());
            dir.setMeta(metaDir);
            if (o.getChildren() != null) {
                List<MenuOutput> menuList = new ArrayList<>();
                for (SysResources s : o.getChildren()) {
                    if (isResourceShow(s.getId())) {
                        MenuOutput menu = new MenuOutput();
                        menu.setPath("/" + o.getName() + "/" + s.getName());
                        menu.setName(o.getName() + "_" + s.getName());
                        menu.setComponent(s.getComponent());
                        MenuOutput.Meta meta = new MenuOutput().new Meta();
                        meta.setTitle(s.getTitle());
                        meta.setIcon(s.getIcon());
                        menu.setMeta(meta);
                        menuList.add(menu);
                    }
                }
                dir.setChildren(menuList);
            }
            finalList.add(dir);
        }
        return finalList;
    }

    //菜单是否显示
    private boolean isResourceShow(Integer resourceId) {
        SysRoleResourcesExample example = new SysRoleResourcesExample();
        SysRoleResourcesExample.Criteria criteria = example.createCriteria();
        criteria.andRoleIdEqualTo(UserRequest.getCurrentRoleId());
        criteria.andResourceIdEqualTo((long) resourceId);
        SysRoleResourcesExample.Criteria criteria2 = example.createCriteria();
        criteria2.andRoleIdEqualTo(UserRequest.getCurrentRoleId());
        criteria2.andParentIdEqualTo((long) resourceId);
        example.or(criteria2);
        List<SysRoleResources> list = sysRoleResourcesMapper.selectByExample(example);
        if (list.size() > 0) {
            return true;
        }
        return false;
    }
}
