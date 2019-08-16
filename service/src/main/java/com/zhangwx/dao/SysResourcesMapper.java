package com.zhangwx.dao;

import com.zhangwx.model.SysResources;
import com.zhangwx.model.SysResourcesExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysResourcesMapper {
    int countByExample(SysResourcesExample example);

    int deleteByExample(SysResourcesExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SysResources record);

    int insertSelective(SysResources record);

    List<SysResources> selectByExample(SysResourcesExample example);

    SysResources selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SysResources record, @Param("example") SysResourcesExample example);

    int updateByExample(@Param("record") SysResources record, @Param("example") SysResourcesExample example);

    int updateByPrimaryKeySelective(SysResources record);

    int updateByPrimaryKey(SysResources record);
}