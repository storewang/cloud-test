package com.ai.spring.boot.content.ribbon.nacos;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.core.Balancer;

import java.util.List;

/**
 * 扩展nacos Balancer类
 * 此类在nacos里面已经实现了
 * @see com.alibaba.cloud.nacos.ribbon.ExtendBalancer
 * @author 石头
 * @Date 2020/3/26
 * @Version 1.0
 **/
public class ExtendNacosBalancer extends Balancer {
    public static Instance getHostByRandomWeightForPublic(List<Instance> hosts) {
        return getHostByRandomWeight(hosts);
    }
}
