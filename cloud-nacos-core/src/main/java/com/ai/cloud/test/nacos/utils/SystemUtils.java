package com.ai.cloud.test.nacos.utils;/**
 * Created by 石头 on 2019/3/4.
 */

import static com.ai.cloud.test.nacos.utils.Constants.FUNCTION_MODE_PROPERTY_NAME;
import static com.ai.cloud.test.nacos.utils.Constants.STANDALONE_MODE_PROPERTY_NAME;

/**
 * 系统工具类
 *
 * @Author 石头
 * @Date 2019/3/4
 * @Version 1.0
 **/
public class SystemUtils {
    /**
     * Standalone mode or not
     */
    public static boolean STANDALONE_MODE = Boolean.getBoolean(STANDALONE_MODE_PROPERTY_NAME);

    public static final String STANDALONE_MODE_ALONE = "standalone";
    public static final String STANDALONE_MODE_CLUSTER = "cluster";

    /**
     * server
     */
    public static String FUNCTION_MODE = System.getProperty(FUNCTION_MODE_PROPERTY_NAME);

    public static final String FUNCTION_MODE_CONFIG = "config";
    public static final String FUNCTION_MODE_NAMING = "naming";
}
