package com.ai.spring.boot.netty.ws.conf;

import com.ai.spring.boot.netty.ws.util.Consts;
import com.ai.spring.boot.netty.ws.util.HostAddressUtil;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * websocket相关配置
 *
 * @author 石头
 * @Date 2020/6/28
 * @Version 1.0
 **/
@Setter
@ConfigurationProperties(prefix = "spring.boot.ws")
public class ServerProperties {
    private static final int DEF_IO_THREADS = Math.min(Runtime.getRuntime().availableProcessors() + 1, 32);
    private static final String DEF_CONTEXT = "/ws";
    private String host;
    private Integer port;
    @Value("${server.port:8080}")
    private Integer webPort;
    @Autowired
    private ThreadProperties thread;
    private String contextPath;

    public Integer getBossThreadNum(){
        return getInteger(thread.getBossThreadNum(),1);
    }
    public Integer getWorkerThreadNum(){
        return getInteger(thread.getWorkerThreadNum(),DEF_IO_THREADS);
    }
    public Integer getMonitorPeriod(){
        return getInteger(thread.getMonitorPeriodSec(),30);
    }
    public Integer getHeartBeatTime(){
        return getInteger(thread.getHeartBeatTimeSec(),1800);
    }
    public Integer getBusinessThreadNum(){
        return getInteger(thread.getBusinessThreadNum(),100);
    }
    public Integer getBusinessQueueSize(){
        return getInteger(thread.getBusinessQueueSize(),1000);
    }

    public String getContextPath(){
        if (contextPath == null || contextPath.length() == 0){
            return DEF_CONTEXT;
        }
        return contextPath;
    }

    public String getWebHost(){
        String host = getHost();
        return host + Consts.STR_SPLIT + webPort;
    }

    public String getHost(){
        if (host == null || host.length()==0){
            return HostAddressUtil.getLocalHostAddress();
        }

        return HostAddressUtil.getHostAddress(host);
    }

    public Integer getPort(){
        if (port == null){
            return HostAddressUtil.getHostPort(host,HostAddressUtil.DEF_PORT);
        }
        return port;
    }

    private Integer getInteger(Integer val,Integer def){
        return val == null ? def : val;
    }
}
