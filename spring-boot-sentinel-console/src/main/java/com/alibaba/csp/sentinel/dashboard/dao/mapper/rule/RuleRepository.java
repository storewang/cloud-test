package com.alibaba.csp.sentinel.dashboard.dao.mapper.rule;

import com.alibaba.csp.sentinel.dashboard.dao.dto.MachineInfo;

import java.util.List;

/**
 * RuleRepository
 *
 * @author 石头
 * @Date 2020/4/26
 * @Version 1.0
 **/
public interface RuleRepository<T, ID> {
    /**
     * Save one.
     *
     * @param entity
     * @return
     */
    T save(T entity);

    /**
     * Save all.
     *
     * @param rules
     * @return rules saved.
     */
    List<T> saveAll(List<T> rules);

    /**
     * Delete by id
     *
     * @param id
     * @return entity deleted
     */
    T delete(ID id);

    /**
     * Find by id.
     *
     * @param id
     * @return
     */
    T findById(ID id);

    /**
     * Find all by machine.
     *
     * @param machineInfo
     * @return
     */
    List<T> findAllByMachine(MachineInfo machineInfo);

    /**
     * Find all by application.
     *
     * @param appName valid app name
     * @return all rules of the application
     * @since 1.4.0
     */
    List<T> findAllByApp(String appName);
}
