package com.alibaba.csp.sentinel.dashboard.service.discovery;

import com.alibaba.csp.sentinel.dashboard.dao.dto.AppInfo;
import com.alibaba.csp.sentinel.dashboard.dao.dto.MachineInfo;

import java.util.List;
import java.util.Set;

/**
 * Machine Discovery
 *
 * @author 石头
 * @Date 2020/4/26
 * @Version 1.0
 **/
public interface MachineDiscovery {
    String UNKNOWN_APP_NAME = "CLUSTER_NOT_STARTED";

    List<String> getAppNames();

    Set<AppInfo> getBriefApps();

    AppInfo getDetailApp(String app);

    /**
     * Remove the given app from the application registry.
     *
     * @param app application name
     * @since 1.5.0
     */
    void removeApp(String app);

    long addMachine(MachineInfo machineInfo);

    /**
     * Remove the given machine instance from the application registry.
     *
     * @param app the application name of the machine
     * @param ip machine IP
     * @param port machine port
     * @return true if removed, otherwise false
     * @since 1.5.0
     */
    boolean removeMachine(String app, String ip, int port);
}
