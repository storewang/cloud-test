package com.alibaba.csp.sentinel.dashboard.vo;

import com.alibaba.csp.sentinel.dashboard.dao.dto.MachineInfo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author 石头
 * @Date 2020/5/6
 * @Version 1.0
 **/
@Data
public class MachineInfoVo {
    private String app;
    private String hostname;
    private String ip;
    private int port;
    private long heartbeatVersion;
    private long lastHeartbeat;
    private boolean healthy;

    private String version;
    public static List<MachineInfoVo> fromMachineInfoList(List<MachineInfo> machines) {
        List<MachineInfoVo> list = new ArrayList<>();
        for (MachineInfo machine : machines) {
            list.add(fromMachineInfo(machine));
        }
        return list;
    }

    public static MachineInfoVo fromMachineInfo(MachineInfo machine) {
        MachineInfoVo vo = new MachineInfoVo();
        vo.setApp(machine.getApp());
        vo.setHostname(machine.getHostname());
        vo.setIp(machine.getIp());
        vo.setPort(machine.getPort());
        vo.setLastHeartbeat(machine.getLastHeartbeat());
        vo.setHeartbeatVersion(machine.getHeartbeatVersion());
        vo.setVersion(machine.getVersion());
        vo.setHealthy(machine.isHealthy());
        return vo;
    }
}
