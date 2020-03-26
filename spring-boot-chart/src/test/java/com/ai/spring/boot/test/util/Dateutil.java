package com.ai.spring.boot.test.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 *
 * @author 石头
 * @Date 2020/3/4
 * @Version 1.0
 **/
public class Dateutil {
    /**
     * 取得今天的日期
     *
     * @return
     */
    public static String getToday() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        return sdf.format(new Date().getTime());
    }

    /**
     * 取得昨天的日期
     *
     * @return
     */
    public static String getYestoday() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date date = calendar.getTime();
        return sdf.format(date.getTime());
    }

    public static String getNow() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        return sdf.format(new Date().getTime());

    }
}
