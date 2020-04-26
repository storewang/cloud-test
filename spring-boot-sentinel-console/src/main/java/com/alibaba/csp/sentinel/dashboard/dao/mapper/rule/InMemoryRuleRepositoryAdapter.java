package com.alibaba.csp.sentinel.dashboard.dao.mapper.rule;

import com.alibaba.csp.sentinel.dashboard.dao.dto.MachineInfo;
import com.alibaba.csp.sentinel.dashboard.dao.entity.rule.RuleEntity;
import com.alibaba.csp.sentinel.util.AssertUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * InMemoryRuleRepository
 *
 * @author 石头
 * @Date 2020/4/26
 * @Version 1.0
 **/
public abstract class InMemoryRuleRepositoryAdapter<T extends RuleEntity> implements RuleRepository<T, Long>{
    /**
     * {@code <machine, <id, rule>>}
     */
    private Map<MachineInfo, Map<Long, T>> machineRules = new ConcurrentHashMap<>(16);
    private Map<Long, T> allRules = new ConcurrentHashMap<>(16);

    private Map<String, Map<Long, T>> appRules = new ConcurrentHashMap<>(16);

    private static final int MAX_RULES_SIZE = 10000;

    @Override
    public T save(T entity) {
        if (entity.getId() == null) {
            entity.setId(nextId());
        }
        T processedEntity = preProcess(entity);
        if (processedEntity != null) {
            allRules.put(processedEntity.getId(), processedEntity);
            machineRules.computeIfAbsent(MachineInfo.of(processedEntity.getApp(), processedEntity.getIp(),
                    processedEntity.getPort()), e -> new ConcurrentHashMap<>(32))
                    .put(processedEntity.getId(), processedEntity);
            appRules.computeIfAbsent(processedEntity.getApp(), v -> new ConcurrentHashMap<>(32))
                    .put(processedEntity.getId(), processedEntity);
        }

        return processedEntity;
    }

    @Override
    public List<T> saveAll(List<T> rules) {
        // TODO: check here.
        allRules.clear();
        machineRules.clear();
        appRules.clear();

        if (rules == null) {
            return null;
        }
        List<T> savedRules = new ArrayList<>(rules.size());
        for (T rule : rules) {
            savedRules.add(save(rule));
        }
        return savedRules;
    }

    @Override
    public T delete(Long id) {
        T entity = allRules.remove(id);
        if (entity != null) {
            if (appRules.get(entity.getApp()) != null) {
                appRules.get(entity.getApp()).remove(id);
            }
            machineRules.get(MachineInfo.of(entity.getApp(), entity.getIp(), entity.getPort())).remove(id);
        }
        return entity;
    }

    @Override
    public T findById(Long id) {
        return allRules.get(id);
    }

    @Override
    public List<T> findAllByMachine(MachineInfo machineInfo) {
        Map<Long, T> entities = machineRules.get(machineInfo);
        if (entities == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(entities.values());
    }

    @Override
    public List<T> findAllByApp(String appName) {
        AssertUtil.notEmpty(appName, "appName cannot be empty");
        Map<Long, T> entities = appRules.get(appName);
        if (entities == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(entities.values());
    }

    public void clearAll() {
        allRules.clear();
        machineRules.clear();
        appRules.clear();
    }

    protected T preProcess(T entity) {
        return entity;
    }

    /**
     * Get next unused id.
     *
     * @return next unused id
     */
    abstract protected long nextId();
}
