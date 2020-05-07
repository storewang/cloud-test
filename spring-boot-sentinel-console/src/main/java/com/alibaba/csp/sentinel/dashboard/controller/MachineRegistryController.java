package com.alibaba.csp.sentinel.dashboard.controller;

import com.alibaba.csp.sentinel.dashboard.dao.dto.MachineInfo;
import com.alibaba.csp.sentinel.dashboard.service.discovery.AppManagement;
import com.alibaba.csp.sentinel.dashboard.service.discovery.MachineDiscovery;
import com.alibaba.csp.sentinel.dashboard.vo.Result;
import com.alibaba.csp.sentinel.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/registry", produces = MediaType.APPLICATION_JSON_VALUE)
public class MachineRegistryController {

    private final Logger logger = LoggerFactory.getLogger(MachineRegistryController.class);

    @Autowired
    private AppManagement appManagement;

    @ResponseBody
    @RequestMapping("/machine")
    public Result<?> receiveHeartBeat(String app, @RequestParam(value = "app_type", required = false, defaultValue = "0") Integer appType, Long version, String v, String hostname, String ip, Integer port) {
        if (app == null) {
            app = MachineDiscovery.UNKNOWN_APP_NAME;
        }
        if (ip == null) {
            return Result.ofFail(-1, "ip can't be null");
        }
        if (port == null) {
            return Result.ofFail(-1, "port can't be null");
        }
        if (port == -1) {
            logger.info("Receive heartbeat from " + ip + " but port not set yet");
            return Result.ofFail(-1, "your port not set yet");
        }
        String sentinelVersion = StringUtil.isEmpty(v) ? "unknown" : v;
        version = version == null ? System.currentTimeMillis() : version;
        try {
            MachineInfo machineInfo = new MachineInfo();
            machineInfo.setApp(app);
            machineInfo.setAppType(appType);
            machineInfo.setHostname(hostname);
            machineInfo.setIp(ip);
            machineInfo.setPort(port);
            machineInfo.setHeartbeatVersion(version);
            machineInfo.setLastHeartbeat(System.currentTimeMillis());
            machineInfo.setVersion(sentinelVersion);
            appManagement.addMachine(machineInfo);
            return Result.ofSuccessMsg("success");
        } catch (Exception e) {
            logger.error("Receive heartbeat error", e);
            return Result.ofFail(-1, e.getMessage());
        }
    }
}
