package com.alibaba.csp.sentinel.dashboard.dao.dto;

import com.alibaba.csp.sentinel.dashboard.util.DashboardConfig;
import lombok.Data;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AppInfo
 *
 * @author 石头
 * @Date 2020/4/26
 * @Version 1.0
 **/
@Data
public class AppInfo {
    private String app = "";

    private Integer appType = 0;

    private Set<MachineInfo> machines = ConcurrentHashMap.newKeySet();

    public AppInfo() {}

    public AppInfo(String app) {
        this.app = app;
    }

    public AppInfo(String app, Integer appType) {
        this.app = app;
        this.appType = appType;
    }

    public boolean addMachine(MachineInfo machineInfo) {
        machines.remove(machineInfo);
        return machines.add(machineInfo);
    }

    public synchronized boolean removeMachine(String ip, int port) {
        Iterator<MachineInfo> it = machines.iterator();
        while (it.hasNext()) {
            MachineInfo machine = it.next();
            if (machine.getIp().equals(ip) && machine.getPort() == port) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public Optional<MachineInfo> getMachine(String ip, int port) {
        return machines.stream()
                .filter(e -> e.getIp().equals(ip) && e.getPort().equals(port))
                .findFirst();
    }

    private boolean heartbeatJudge(final int threshold) {
        if (machines.size() == 0) {
            return false;
        }
        if (threshold > 0) {
            long healthyCount = machines.stream()
                    .filter(MachineInfo::isHealthy)
                    .count();
            if (healthyCount == 0) {
                // No healthy machines.
                return machines.stream()
                        .max(Comparator.comparingLong(MachineInfo::getLastHeartbeat))
                        .map(e -> System.currentTimeMillis() - e.getLastHeartbeat() < threshold)
                        .orElse(false);
            }
        }
        return true;
    }

    /**
     * Check whether current application has no healthy machines and should not be displayed.
     *
     * @return true if the application should be displayed in the sidebar, otherwise false
     */
    public boolean isShown() {
        return heartbeatJudge(DashboardConfig.getHideAppNoMachineMillis());
    }

    /**
     * Check whether current application has no healthy machines and should be removed.
     *
     * @return true if the application is dead and should be removed, otherwise false
     */
    public boolean isDead() {
        return !heartbeatJudge(DashboardConfig.getRemoveAppNoMachineMillis());
    }
}
