package com.ai.spring.boot.im.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by 石头 on 2020/9/12.
 */
@ConfigurationProperties(prefix = "wx")
@Data
public class WxConf {
    private String appId;
    private String appsecret;
    private String token;
}
