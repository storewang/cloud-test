package com.ai.cloud.test.nacos.utils;/**
 * Created by 石头 on 2019/3/4.
 */

import java.io.InputStream;
import java.util.Properties;

/**
 * 获取nacos的版本信息
 *
 * @Author 石头
 * @Date 2019/3/4
 * @Version 1.0
 **/
public class VersionUtils {
    public static String VERSION;
    /**获取当前version*/
    public static final String VERSION_DEFAULT = "${project.version}";
    static{
        try{
            InputStream in = VersionUtils.class.getClassLoader().getResourceAsStream("nacos-version.txt");
            Properties props = new Properties();
            props.load(in);
            String val = props.getProperty("version");
            if (val != null && !VERSION_DEFAULT.equals(val)) {
                VERSION = val;
            }
        } catch(Exception e) {
            VERSION = "unknow";
        }
    }
}
