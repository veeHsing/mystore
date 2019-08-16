package com.zhangwx.dao;

import com.zhangwx.model.SysRoleResources;
import com.zhangwx.model.SysRoleResourcesExample;
import com.zhangwx.model.SysRoleResouresMap;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysRoleResourcesMapper {
    int countByExample(SysRoleResourcesExample example);

    int deleteByExample(SysRoleResourcesExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SysRoleResources record);

    int insertSelective(SysRoleResources record);

    List<SysRoleResources> selectByExample(SysRoleResourcesExample example);

    SysRoleResources selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SysRoleResources record, @Param("example") SysRoleResourcesExample example);

    int updateByExample(@Param("record") SysRoleResources record, @Param("example") SysRoleResourcesExample example);

    int updateByPrimaryKeySelective(SysRoleResources record);

    int updateByPrimaryKey(SysRoleResources record);

    @Select("SELECT sr.path as code,GROUP_CONCAT(r.code) as rule FROM sys_role_resources as srr \n" +
            "LEFT JOIN sys_resources sr ON srr.resource_id=sr.id  \n" +
            "LEFT JOIN sys_role r ON srr.role_id=r.id\n" +
            "WHERE srr.deleted=0 GROUP BY srr.resource_id ")
    List<SysRoleResouresMap> getFilterChainDefinitionMap();

    List<SysRoleResources> selectBySimpleExample(SysRoleResourcesExample example);
}