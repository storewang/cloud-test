package com.ai.spring.boot.content.ribbon.nacos;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 整合nacos实现权重负载均衡
 *
 * @author 石头
 * @Date 2020/3/26
 * @Version 1.0
 **/
@Slf4j
public class NacosWeightedRule extends AbstractLoadBalancerRule{
    @Autowired
    private NacosDiscoveryProperties discoveryProperties;
    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {
    }

    @Override
    public Server choose(Object key) {
        try{
            BaseLoadBalancer loadBalancer = (BaseLoadBalancer)this.getLoadBalancer();
            // 拿到服务发现相关api
            NamingService namingService = discoveryProperties.namingServiceInstance();
            Instance instance = namingService.selectOneHealthyInstance(loadBalancer.getName());

            log.info("选择的实例是：port={},instance={}",instance.getPort(),instance);
            return new NacosServer(instance);
        }catch (NacosException e){
        }

        return null;
    }
}
