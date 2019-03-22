package com.ai.cloud.test.nacos.sentinel.datasource;

import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.init.InitFunc;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.nacos.api.PropertyKeyConst;

import java.util.List;
import java.util.Properties;

public class DataSourceInitFunc implements InitFunc {
    @Override
    public void init() throws Exception {
        final String remoteAddress = "127.0.0.1:8848";
        final String groupId   = "Sentinel:Demo";
        final String dataId    = "com.alibaba.csp.sentinel.demo.flow.rule.json";
        final String nameSpace = "57b4356e-76c8-49c1-a527-d1d466327842";

        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR,remoteAddress);
        properties.put(PropertyKeyConst.NAMESPACE,nameSpace);

        ReadableDataSource<String, List<FlowRule>> flowRuleDataSource =
                new NacosDataSource<>(properties,groupId,dataId,source -> JSON.parseObject(source,new TypeReference<List<FlowRule>>(){}));

        //WritableDataSource<List<FlowRule>> flowRuleDataSource;
        FlowRuleManager.register2Property(flowRuleDataSource.getProperty());
    }
}
