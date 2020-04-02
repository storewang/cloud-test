package com.ai.spring.boot.content.conf.sentinel.nacos;

import com.ai.spring.boot.content.conf.SentinelConf;
import com.ai.spring.boot.content.util.SentinelConfUtil;
import com.alibaba.csp.sentinel.command.handler.ModifyParamFlowRulesCommandHandler;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.datasource.WritableDataSource;
import com.alibaba.csp.sentinel.init.InitFunc;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
import com.alibaba.csp.sentinel.transport.util.WritableDataSourceRegistry;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * setinel配置持久化（拉模式）,nacos存储
 *
 * @author 石头
 * @Date 2020/4/1
 * @Version 1.0
 **/
@Slf4j
public class NacosDataSourceInit implements InitFunc {
    /** json转换相关配置 */
    private Converter<String,List<FlowRule>> flowRuleListParser = source -> JSON.parseObject(source,new TypeReference<List<FlowRule>>(){});
    private Converter<String,List<DegradeRule>> degradeRuleListParser = source -> JSON.parseObject(source,new TypeReference<List<DegradeRule>>(){});
    private Converter<String,List<SystemRule>> systemRuleListParser = source -> JSON.parseObject(source,new TypeReference<List<SystemRule>>(){});
    private Converter<String,List<AuthorityRule>> authorityRuleListParser = source -> JSON.parseObject(source,new TypeReference<List<AuthorityRule>>(){});
    private Converter<String,List<ParamFlowRule>> paramFlowRuleListParser = source -> JSON.parseObject(source,new TypeReference<List<ParamFlowRule>>(){});

    @Override
    public void init() throws Exception {
        SentinelConf sentinelConf = SentinelConfUtil.getSentinelConfUtil().getSentinelConf();

        // 流控规则
        // 读
        SentinelConf flowRuleConf = getRuleConf(sentinelConf,sentinelConf.getFlowDataId());
        NacosReadDataSource<List<FlowRule>> flowRuleRDS = getReadRuleDs(flowRuleConf,flowRuleListParser);
        // 将可读数据源注册到FlowRuleManager
        FlowRuleManager.register2Property(flowRuleRDS.getProperty());
        // 写
        WritableDataSource<List<FlowRule>> flowRuleWDS = new NacosWritableDataSource<>(flowRuleConf,this::encodeJosn);
        // 这样收到控制台推送的规则时，sentinel会先更新到内存，然后将规则写入到文件中。
        WritableDataSourceRegistry.registerFlowDataSource(flowRuleWDS);

        // 降级规则
        // 读
        SentinelConf degradeRuleConf = getRuleConf(sentinelConf,sentinelConf.getDegradeDataId());
        NacosReadDataSource<List<DegradeRule>> degradeRuleRDS = getReadRuleDs(degradeRuleConf,degradeRuleListParser);
        // 将可读数据源注册到FlowRuleManager
        DegradeRuleManager.register2Property(degradeRuleRDS.getProperty());
        // 写
        WritableDataSource<List<DegradeRule>> degradeRuleWDS = new NacosWritableDataSource<>(degradeRuleConf,this::encodeJosn);
        WritableDataSourceRegistry.registerDegradeDataSource(degradeRuleWDS);

        // 系统规则
        // 读
        SentinelConf systemRuleConf = getRuleConf(sentinelConf,sentinelConf.getSystemDataId());
        NacosReadDataSource<List<SystemRule>> systemRuleRDS = getReadRuleDs(systemRuleConf,systemRuleListParser);
        // 将可读数据源注册到FlowRuleManager
        SystemRuleManager.register2Property(systemRuleRDS.getProperty());
        // 写
        WritableDataSource<List<SystemRule>> systemRuleWDS = new NacosWritableDataSource<>(systemRuleConf,this::encodeJosn);
        WritableDataSourceRegistry.registerSystemDataSource(systemRuleWDS);

        // 授权规则
        // 读
        SentinelConf authorityRuleConf = getRuleConf(sentinelConf,sentinelConf.getAuthorityDataId());
        NacosReadDataSource<List<AuthorityRule>> authorityRuleRDS = getReadRuleDs(authorityRuleConf,authorityRuleListParser);
        // 将可读数据源注册到FlowRuleManager
        AuthorityRuleManager.register2Property(authorityRuleRDS.getProperty());
        // 写
        WritableDataSource<List<AuthorityRule>> authorityRuleWDS = new NacosWritableDataSource<>(authorityRuleConf,this::encodeJosn);
        WritableDataSourceRegistry.registerAuthorityDataSource(authorityRuleWDS);

        // 热点参数规则
        // 读
        SentinelConf paramFlowRuleConf = getRuleConf(sentinelConf,sentinelConf.getParamDataId());
        NacosReadDataSource<List<ParamFlowRule>> paramFlowRuleRDS = getReadRuleDs(paramFlowRuleConf,paramFlowRuleListParser);
        // 将可读数据源注册到FlowRuleManager
        ParamFlowRuleManager.register2Property(paramFlowRuleRDS.getProperty());
        // 写
        WritableDataSource<List<ParamFlowRule>> paramFlowRuleWDS = new NacosWritableDataSource<>(paramFlowRuleConf,this::encodeJosn);
        ModifyParamFlowRulesCommandHandler.setWritableDataSource(paramFlowRuleWDS);
    }

    private <T>NacosReadDataSource<T> getReadRuleDs(SentinelConf conf,Converter<String, T> parser){
        return new NacosReadDataSource<T>(conf,parser);
    }
    private SentinelConf getRuleConf(SentinelConf sentinelConf,String dataId){
        SentinelConf ruleConf = new SentinelConf();
        ruleConf.setDataId(SentinelConfUtil.getSentinelConfUtil().getApplicationName() + "-" + dataId);
        ruleConf.setGroupId(sentinelConf.getGroupId());
        ruleConf.setNacosServer(sentinelConf.getNacosServer());
        return ruleConf;
    }

    private <T> String encodeJosn(T t){
        return JSON.toJSONString(t);
    }
}
