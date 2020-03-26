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
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 相同版本的服务调用
 *
 * @author 石头
 * @Date 2020/3/26
 * @Version 1.0
 **/
@Slf4j
public class NacosSameVersionWeightedRule extends AbstractLoadBalancerRule {
    private static final String TARGET_VERSION = "target-version";
    private static final String SERVICE_VERSION= "version";

    @Autowired
    private NacosDiscoveryProperties discoveryProperties;

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {
    }

    @Override
    public Server choose(Object key) {
        // 负载均衡规则：优先选择同集群下，符合metadata的实例
        // 如果没有，就选择所有集群下，符合metadata的实例

        // 1. 查询所有实例 A
        // 2. 筛选元数据匹配的实例 B
        // 3. 筛选出同cluster下元数据匹配的实例 C
        // 4. 如果C为空，就用B
        // 5. 随机选择实例
        try{
            BaseLoadBalancer loadBalancer = (BaseLoadBalancer)this.getLoadBalancer();
            String clusterName   = discoveryProperties.getClusterName();
            String serviceName   = loadBalancer.getName();
            String targetVersion = discoveryProperties.getMetadata().get(TARGET_VERSION);
            // 拿到服务发现相关api
            NamingService namingService = discoveryProperties.namingServiceInstance();

            // 所有实例
            List<Instance> instances = namingService.selectInstances(serviceName, true);

            List<Instance> metadataMatchInstances = instances;
            // 配置了版本映射，那么只能调用相同版本的服务
            if (!StringUtils.isEmpty(targetVersion)){
                metadataMatchInstances = instances.stream().filter(instance -> Objects.equals(targetVersion,instance.getMetadata().get(SERVICE_VERSION))).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(metadataMatchInstances)){
                    log.warn("未找到相同版本的服务实例，请检查配置。targetVersion = {},instance = {}",targetVersion,instances);
                    return null;
                }
            }

            List<Instance> clusterMetadataMatchInstances = metadataMatchInstances;
            //如果配置了集群名称，则优先调用相同集群下的服务
            if (!StringUtils.isEmpty(clusterName)){
                clusterMetadataMatchInstances = metadataMatchInstances.stream().filter(instance -> Objects.equals(clusterName,instance.getClusterName())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(clusterMetadataMatchInstances)){
                    clusterMetadataMatchInstances = metadataMatchInstances;
                    log.warn("发生跨集群调用，clusterName = {},targetVersion = {},clusterMetadataMatchInstances = {}",clusterName,targetVersion,clusterMetadataMatchInstances);
                }
            }

            //基于权重的负载均衡算法，返回1个实例
            Instance instance = ExtendNacosBalancer.getHostByRandomWeightForPublic(clusterMetadataMatchInstances);
            log.info("选择的实例是：port={},instance={}",instance.getPort(),clusterMetadataMatchInstances);

            return new NacosServer(instance);
        }catch (NacosException e){
        }
        return null;
    }
}
