package com.ai.spring.im.common.util;

/**
 * 字符串工具类
 *
 * @author 石头
 * @Date 2020/6/29
 * @Version 1.0
 **/
public final class StringUtil {
    public static final String EMPTY = "";
    public static final String LF = "\n";
    public static final String CR = "\r";
    public static final int INDEX_NOT_FOUND = -1;

    /**
     * 截取分隔符之前的字符串
     * @param str
     * @param separator
     * @return
     */
    public static String substringBefore(final String str, final String separator) {
        if (isEmpty(str) || separator == null) {
            return str;
        }
        if (separator.isEmpty()) {
            return EMPTY;
        }
        final int pos = str.indexOf(separator);
        if (pos == INDEX_NOT_FOUND) {
            return str;
        }
        return str.substring(0, pos);
    }

    /**
     * 判断字符串是否为空
     * @param cs
     * @return
     */
    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    /**
     * 字符串转Long型
     * @param str
     * @return
     */
    public static Long str2Long(String str){
        try {
            return Long.parseLong(str);
        }catch (Throwable e){
            return null;
        }
    }
}
