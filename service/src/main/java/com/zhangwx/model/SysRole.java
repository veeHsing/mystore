package com.zhangwx.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

public class SysRole {

    public interface add{};
    public interface update{};

    private Long id;

    @NotBlank(message = "角色不能为空",groups = {add.class,update.class})
    private String name;
    @NotBlank(message = "代码不能为空",groups = {add.class,update.class})
    private String code;

    private String remark;

    private Long createBy;

    private Date createAt;

    private Date updateAt;

    private Long updateBy;

    private Byte deleted;

    private Long areaId;

    private List<SysResources> resourcesList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Byte getDeleted() {
        return deleted;
    }

    public void setDeleted(Byte deleted) {
        this.deleted = deleted;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public List<SysResources> getResourcesList() {
        return resourcesList;
    }

    public void setResourcesList(List<SysResources> resourcesList) {
        this.resourcesList = resourcesList;
    }
}