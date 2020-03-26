package com.ai.spring.boot.content.conf;

import com.ai.spring.boot.ribbon.conf.RibbonConf;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Configuration;

/**
 * 用户服务负载策略配置
 *
 * @author 石头
 * @Date 2020/3/26
 * @Version 1.0
 **/
@Configuration
@RibbonClient(name = "spring-boot-user",configuration = RibbonConf.class)
// 全局配置方式
//@RibbonClients(defaultConfiguration = RibbonConf.class)
public class UserRibbonConf {
}
