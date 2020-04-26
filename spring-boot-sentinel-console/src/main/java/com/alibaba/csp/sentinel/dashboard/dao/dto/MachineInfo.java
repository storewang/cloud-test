package com.alibaba.csp.sentinel.dashboard.dao.dto;

import com.alibaba.csp.sentinel.dashboard.util.DashboardConfig;
import com.alibaba.csp.sentinel.util.StringUtil;
import lombok.Data;

import java.util.Objects;

/**
 * MachineInfo
 *
 * @author 石头
 * @Date 2020/4/26
 * @Version 1.0
 **/
@Data
public class MachineInfo implements Comparable<MachineInfo>{
    private String app = "";
    private Integer appType = 0;
    private String hostname = "";
    private String ip = "";
    private Integer port = -1;
    private long lastHeartbeat;
    private long heartbeatVersion;

    /**
     * Indicates the version of Sentinel client (since 0.2.0).
     */
    private String version;

    public static MachineInfo of(String app, String ip, Integer port) {
        MachineInfo machineInfo = new MachineInfo();
        machineInfo.setApp(app);
        machineInfo.setIp(ip);
        machineInfo.setPort(port);
        return machineInfo;
    }

    public String toHostPort() {
        return ip + ":" + port;
    }

    public MachineInfo setVersion(String version) {
        this.version = version;
        return this;
    }

    public boolean isHealthy() {
        long delta = System.currentTimeMillis() - lastHeartbeat;
        return delta < DashboardConfig.getUnhealthyMachineMillis();
    }

    /**
     * whether dead should be removed
     *
     * @return
     */
    public boolean isDead() {
        if (DashboardConfig.getAutoRemoveMachineMillis() > 0) {
            long delta = System.currentTimeMillis() - lastHeartbeat;
            return delta > DashboardConfig.getAutoRemoveMachineMillis();
        }
        return false;
    }

    @Override
    public int compareTo(MachineInfo o) {
        if (this == o) {
            return 0;
        }
        if (!port.equals(o.getPort())) {
            return port.compareTo(o.getPort());
        }
        if (!StringUtil.equals(app, o.getApp())) {
            return app.compareToIgnoreCase(o.getApp());
        }
        return ip.compareToIgnoreCase(o.getIp());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof MachineInfo)) { return false; }
        MachineInfo that = (MachineInfo)o;
        return Objects.equals(app, that.app) &&
                Objects.equals(ip, that.ip) &&
                Objects.equals(port, that.port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(app, ip, port);
    }

    /**
     * Information for log
     *
     * @return
     */
    public String toLogString() {
        return app + "|" + ip + "|" + port + "|" + version;
    }
}
