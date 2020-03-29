
package com.ai.spring.boot.content.util;

import com.ai.spring.boot.content.conf.SentinelConf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by yoyoo on 2020/3/29.
 */
@Component
public class SentinelConfUtil {
    @Autowired
    private SentinelConf sentinelConf;
    private static SentinelConfUtil sentinelConfUtil;
    @PostConstruct
    public void init(){
        sentinelConfUtil = this;
    }

    public SentinelConf getSentinelConf() {
        return sentinelConf;
    }

    public static SentinelConfUtil getSentinelConfUtil(){
        return sentinelConfUtil;
    }

}
