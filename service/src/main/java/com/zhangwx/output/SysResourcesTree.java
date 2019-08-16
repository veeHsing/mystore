package com.zhangwx.output;

import com.zhangwx.model.SysResources;

import java.util.List;

public class SysResourcesTree extends SysResources {

    private List<SysResources> children;

    public List<SysResources> getChildren() {
        return children;
    }

    public void setChildren(List<SysResources> children) {
        this.children = children;
    }
}
