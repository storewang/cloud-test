package com.ai.spring.boot.content.conf;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by yoyoo on 2020/3/29.
 */
@Data
@Component
@Slf4j
public class SentinelConf {
    @Value("${spring.cloud.sentinel.rules.base-path:/data/sentinel/rules}")
    private String basePath;
    /**nacos配置服务器地址*/
    @Value("${spring.cloud.sentinel.nacos.server}")
    private String nacosServer;
    /**用于存储sentinel配置的groupId*/
    @Value("${spring.cloud.sentinel.nacos.groupId}")
    private String groupId;
    @Value("${spring.cloud.sentinel.nacos.dataId.flow}")
    private String flowDataId;
    @Value("${spring.cloud.sentinel.nacos.dataId.authority}")
    private String authorityDataId;
    @Value("${spring.cloud.sentinel.nacos.dataId.degrade}")
    private String degradeDataId;
    @Value("${spring.cloud.sentinel.nacos.dataId.param}")
    private String paramDataId;
    @Value("${spring.cloud.sentinel.nacos.dataId.system}")
    private String systemDataId;
    @Value("${spring.cloud.sentinel.nacos.timeout:3000}")
    private Integer readTimeout;

    /**用于存储sentinel配置的dataId*/
    private String dataId;

    @PostConstruct
    public void init(){
        log.info("---------sentinel.rules.basePath:{}---------",basePath);
        log.info("---------sentinel.nacosServer:{}---------",nacosServer);
        log.info("---------sentinel.groupId:{}---------",groupId);
        log.info("---------sentinel.readTimeout:{}---------",readTimeout);
    }
}
