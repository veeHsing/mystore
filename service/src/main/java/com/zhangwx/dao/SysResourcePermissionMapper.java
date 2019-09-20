package com.zhangwx.dao;

import com.zhangwx.model.SysResourcePermission;
import com.zhangwx.model.SysResourcePermissionExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysResourcePermissionMapper {
    int countByExample(SysResourcePermissionExample example);

    int deleteByExample(SysResourcePermissionExample example);

    int insert(SysResourcePermission record);

    int insertSelective(SysResourcePermission record);

    List<SysResourcePermission> selectByExample(SysResourcePermissionExample example);

    int updateByExampleSelective(@Param("record") SysResourcePermission record, @Param("example") SysResourcePermissionExample example);

    int updateByExample(@Param("record") SysResourcePermission record, @Param("example") SysResourcePermissionExample example);
}