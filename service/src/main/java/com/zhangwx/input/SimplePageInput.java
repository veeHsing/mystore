package com.zhangwx.input;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;

public class SimplePageInput {
    @Min(value = 0,message = "页码不能小于0")
    private int page;

    @Min(value = 10,message = "分页查询不能小于10条记录")
    @Max(value = 50,message = "分页查询不能超过50条记录")
    private int limit;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
