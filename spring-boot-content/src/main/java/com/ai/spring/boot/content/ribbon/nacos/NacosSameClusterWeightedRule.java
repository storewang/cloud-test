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
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 整合nacos实现相同集群优先调用的权重负载均衡
 *
 * @author 石头
 * @Date 2020/3/26
 * @Version 1.0
 **/
@Slf4j
public class NacosSameClusterWeightedRule extends AbstractLoadBalancerRule {
    @Autowired
    private NacosDiscoveryProperties discoveryProperties;

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }

    @Override
    public Server choose(Object key) {
        try{
            BaseLoadBalancer loadBalancer = (BaseLoadBalancer)this.getLoadBalancer();
            String clusterName = discoveryProperties.getClusterName();
            // 拿到服务发现相关api
            NamingService namingService = discoveryProperties.namingServiceInstance();
            List<Instance> instancesToBeChosen = new ArrayList<>();
            //1. 找到指定的服务的所有实例 A
            List<Instance> instances = namingService.selectInstances(loadBalancer.getName(), true);
            //2. 过滤出相同集群下的所有实例 B
            List<Instance> sameClusterInstances = instances.stream().filter(instance -> Objects.equals(instance.getClusterName(), clusterName)).collect(Collectors.toList());
            //3. 如果B为空，就用A
            if (CollectionUtils.isEmpty(sameClusterInstances)){
                instancesToBeChosen = instances;
                log.warn("发生跨集群调用,name = {},clusterName = {}, instances = {}",loadBalancer.getName(),clusterName,instances);
            }else {
                instancesToBeChosen = sameClusterInstances;
            }
            //4. 基于权重的负载均衡算法，返回1个实例
            Instance instance = ExtendNacosBalancer.getHostByRandomWeightForPublic(instancesToBeChosen);
            log.info("选择的实例是：port={},instance={}",instance.getPort(),instance);

            return new NacosServer(instance);
        }catch (NacosException e){
        }
        return null;
    }

}
