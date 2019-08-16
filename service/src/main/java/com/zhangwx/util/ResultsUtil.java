package com.zhangwx.util;

import com.zhangwx.base.Result;
import com.zhangwx.constants.MyExceptionCode;

/**
 * 返回对象
 */
public class ResultsUtil {

    public static Result success(){
        Result result =new Result();
        result.setCode(0);
        result.setMsg("成功");
        return result;
    }

    public static Result success(String msg) {
        Result result =new Result();
        result.setCode(0);
        result.setMsg(msg);
        return result;
    }

    public static Result success(Object data){
        Result result =new Result();
        result.setCode(0);
        result.setMsg("成功");
        result.setData(data);
        return result;
    }

    public static Result success(String msg,Object data){
        Result result =new Result();
        result.setCode(0);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static Result failure(){
        Result result =new Result();
        result.setCode(-1);
        result.setMsg("失败");
        return result;
    }

    public static Result failure(Integer code){
        Result result =new Result();
        result.setCode(code);
        result.setMsg("失败");
        return result;
    }

    public static Result failure(Integer code,String msg){
        Result result =new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public static Result failure(String msg){
        Result result =new Result();
        result.setCode(-1);
        result.setMsg(msg);
        return result;
    }

    public static Result failure(MyExceptionCode e){
        Result result =new Result();
        result.setCode(e.getCode());
        result.setMsg(e.getMsg());
        return result;
    }


}
