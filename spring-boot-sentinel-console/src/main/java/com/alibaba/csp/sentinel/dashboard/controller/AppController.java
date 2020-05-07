package com.alibaba.csp.sentinel.dashboard.controller;

import com.alibaba.csp.sentinel.dashboard.dao.dto.AppInfo;
import com.alibaba.csp.sentinel.dashboard.dao.dto.MachineInfo;
import com.alibaba.csp.sentinel.dashboard.service.discovery.AppManagement;
import com.alibaba.csp.sentinel.dashboard.vo.MachineInfoVo;
import com.alibaba.csp.sentinel.dashboard.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 应用控制器
 *
 * @author 石头
 * @Date 2020/5/6
 * @Version 1.0
 **/
@RestController
@RequestMapping(value = "/app")
public class AppController {
    @Autowired
    private AppManagement appManagement;

    @GetMapping("/names.json")
    public Result<List<String>> queryApps(HttpServletRequest request) {
        return Result.ofSuccess(appManagement.getAppNames());
    }
    @GetMapping("/briefinfos.json")
    public Result<List<AppInfo>> queryAppInfos(HttpServletRequest request) {
        List<AppInfo> list = new ArrayList<>(appManagement.getBriefApps());
        Collections.sort(list, Comparator.comparing(AppInfo::getApp));
        return Result.ofSuccess(list);
    }

    @GetMapping(value = "/{app}/machines.json")
    public Result<List<MachineInfoVo>> getMachinesByApp(@PathVariable("app") String app) {
        AppInfo appInfo = appManagement.getDetailApp(app);
        if (appInfo == null) {
            return Result.ofSuccess(null);
        }
        List<MachineInfo> list = new ArrayList<>(appInfo.getMachines());
        Collections.sort(list, Comparator.comparing(MachineInfo::getApp).thenComparing(MachineInfo::getIp).thenComparingInt(MachineInfo::getPort));
        return Result.ofSuccess(MachineInfoVo.fromMachineInfoList(list));
    }

    @RequestMapping(value = "/{app}/machine/remove.json")
    public Result<String> removeMachineById(
            @PathVariable("app") String app,
            @RequestParam(name = "ip") String ip,
            @RequestParam(name = "port") int port) {
        AppInfo appInfo = appManagement.getDetailApp(app);
        if (appInfo == null) {
            return Result.ofSuccess(null);
        }
        if (appManagement.removeMachine(app, ip, port)) {
            return Result.ofSuccessMsg("success");
        } else {
            return Result.ofFail(1, "remove failed");
        }
    }
}
