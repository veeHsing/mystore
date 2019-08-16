package com.zhangwx.core;

import com.alibaba.fastjson.JSON;
import com.zhangwx.base.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 配置文件抛出异常
 * response返回json数据
 */
public class ResponseResult {

    private static final Logger logger=LoggerFactory.getLogger(ResponseResult.class);

    public static void responseResult(ServletResponse response, Result result) {

        response.setContentType("application/json;charset=utf-8");//指定返回的格式为JSON格式
        response.setCharacterEncoding("UTF-8");//setContentType与setCharacterEncoding的顺序不能调换，否则还是无法解决中文乱码的问题

        PrintWriter out =null ;
        try {
            out =response.getWriter() ;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        out.write(JSON.toJSONString(result));
        out.close();
        return ;

    }
}
