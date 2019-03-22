package com.ai.cloud.test.sentinel.repository.rule;

import com.ai.cloud.test.sentinel.datasource.NacosDataSource;
import com.ai.cloud.test.sentinel.datasource.entity.rule.FlowRuleEntity;
import com.ai.cloud.test.sentinel.discovery.MachineInfo;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TODO
 *
 * @Author 石头
 * @Date 2019/3/19
 * @Version 1.0
 **/
@Service("nacosFlowRuleStore")
@Slf4j
public class NacosFlowRuleStore extends NacosRuleRepositoryAdapter<FlowRuleEntity>{
    private NacosDataSource nacosDataSource;
    @Autowired
    @Qualifier("inMemFlowRuleStore")
    private InMemFlowRuleStore inMemFlowRuleStore;
    @Override
    public FlowRuleEntity save(FlowRuleEntity entity) {
        return inMemFlowRuleStore.save(entity);
    }

    @Override
    public List<FlowRuleEntity> saveAll(List<FlowRuleEntity> rules) {
        return inMemFlowRuleStore.saveAll(rules);
    }

    @Override
    public FlowRuleEntity delete(Long aLong) {
        return inMemFlowRuleStore.delete(aLong);
    }

    @Override
    public FlowRuleEntity findById(Long aLong) {
        try {
            String content = nacosDataSource.getConfigService().getConfig(DATAID,GROUP,TIMEOUT);
            log.info("-------------findById-----------{}",content);
        } catch (NacosException e) {
            log.error("findById.error",e);
        }
        return inMemFlowRuleStore.findById(aLong);
    }

    @Override
    public List<FlowRuleEntity> findAllByMachine(MachineInfo machineInfo) {
        return inMemFlowRuleStore.findAllByMachine(machineInfo);
    }
    @Override
    public List<FlowRuleEntity> findAllByApp(String appName) {
        return inMemFlowRuleStore.findAllByApp(appName);
    }
}
