package com.zhangwx.util;

import com.zhangwx.constants.DateTimeConstants;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DatesUtil {

    public DatesUtil() {
    }

    public static String now(){
        Date dNow=new Date();
        SimpleDateFormat sdformat=new SimpleDateFormat(DateTimeConstants.YYYY_MM_DD_HH_MM_SS);
        return  sdformat.format(dNow);
    }





}
