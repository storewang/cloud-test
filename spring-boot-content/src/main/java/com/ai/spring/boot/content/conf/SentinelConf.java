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

    @PostConstruct
    public void init(){
        log.info("---------sentinel.rules.basePath:{}---------",basePath);
    }
}
