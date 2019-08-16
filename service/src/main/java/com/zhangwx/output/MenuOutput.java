package com.zhangwx.output;

import java.util.List;

/**
 * 后台菜单
 */
public class MenuOutput {

    private String path;//路由
    private String redirect;//重定向路由
    private String name;//路由名称
    private Meta meta;
    private List<MenuOutput> children;


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<MenuOutput> getChildren() {
        return children;
    }

    public void setChildren(List<MenuOutput> children) {
        this.children = children;
    }

    class Meta{
        private String title;
        private String icon;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }


}
