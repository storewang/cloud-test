package com.ai.spring.boot.netty.ws.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * host address helper
 *
 * @author 石头
 * @Date 2020/6/28
 * @Version 1.0
 **/
public final class HostAddressUtil {
    public static final String HOST_SPLIT = ":";
    public static final Integer DEF_PORT  = 10081;

    public static String getLocalHostAddress(){
        String hostAddress = "127.0.0.1";
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            hostAddress = localHost.getHostAddress();
        } catch (UnknownHostException e) {
            hostAddress = "127.0.0.1";
        }
        return hostAddress;
    }

    public static String getHostAddress(String host){
        if (host!=null && host.indexOf(HOST_SPLIT)>-1){
            return host.split(HOST_SPLIT)[0];
        }else if (host!=null){
            return host;
        }
        return null;
    }

    public static Integer getHostPort(String host){
        return getHostPort(host,null);
    }

    public static Integer getHostPort(String host,Integer def){
        if (host!=null && host.indexOf(HOST_SPLIT)>-1){
            return str2Int(host.split(HOST_SPLIT)[1],def);
        }
        return def;
    }
    public static Integer str2Int(String str){
        return str2Int(str,null);
    }
    public static Integer str2Int(String str,Integer def){
        try{
            return Integer.parseInt(str);
        }catch (Exception e){
            return def;
        }
    }
}
