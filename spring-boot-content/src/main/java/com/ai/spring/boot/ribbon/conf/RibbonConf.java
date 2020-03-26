package com.ai.spring.boot.ribbon.conf;

import com.ai.spring.boot.content.ribbon.nacos.NacosSameVersionWeightedRule;
import com.netflix.loadbalancer.IRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ribbon 负载均衡策略
 * 这个配置类需要创建在启动类扫描包之外（不能的话就变成全局配置了）
 * ribbon全局配置有更好的方式进行配置
 * @author 石头
 * @Date 2020/3/26
 * @Version 1.0
 **/
@Configuration
public class RibbonConf {
    @Bean
    public IRule ribbonRule(){
        //return new NacosWeightedRule();
        //return new NacosSameClusterWeightedRule();
        return new NacosSameVersionWeightedRule();
    }

    /**
     * 自定义ping规则,默认是DummyPing
     *
     * @return
     */
//    @Bean
//    public IPing ping(){
//        return new PingUrl();
//    }
}
