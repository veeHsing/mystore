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

    @Select("SELECT b.route as code,c.`code` as rule FROM sys_role_resources a \n" +
            "LEFT JOIN sys_resource_permission b ON (a.resource_id=b.resource_id OR a.parent_id=b.resource_id)\n" +
            "LEFT JOIN sys_role  c ON a.role_id=c.id")
    List<SysRoleResouresMap> getFilterChainDefinitionMap();

    List<SysRoleResources> selectBySimpleExample(SysRoleResourcesExample example);
}