package com.zhangwx.input;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class ResourcePermissionInput {
    @NotNull(message = "id不能为空")
    private int resourceId;
    private List<String> routeList;

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public List<String> getRouteList() {
        return routeList;
    }

    public void setRouteList(List<String> routeList) {
        this.routeList = routeList;
    }
}
