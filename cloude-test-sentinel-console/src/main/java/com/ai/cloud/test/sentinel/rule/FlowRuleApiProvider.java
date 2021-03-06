/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ai.cloud.test.sentinel.rule;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.ai.cloud.test.sentinel.client.SentinelApiClient;
import com.ai.cloud.test.sentinel.datasource.entity.rule.FlowRuleEntity;
import com.ai.cloud.test.sentinel.discovery.AppManagement;
import com.ai.cloud.test.sentinel.discovery.MachineInfo;
import com.ai.cloud.test.sentinel.util.MachineUtils;
import com.alibaba.csp.sentinel.util.StringUtil;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Eric Zhao
 */
@Component("flowRuleDefaultProvider")
public class FlowRuleApiProvider implements DynamicRuleProvider<List<FlowRuleEntity>> {

    @Autowired
    private SentinelApiClient sentinelApiClient;
    @Autowired
    private AppManagement appManagement;

    @Override
    public List<FlowRuleEntity> getRules(String appName) throws Exception {
        if (StringUtil.isBlank(appName)) {
            return new ArrayList<>();
        }
        List<MachineInfo> list = appManagement.getDetailApp(appName).getMachines()
            .stream()
            .filter(MachineUtils::isMachineHealth)
            .sorted((e1, e2) -> {
                if (e1.getTimestamp().before(e2.getTimestamp())) {
                    return 1;
                } else if (e1.getTimestamp().after(e2.getTimestamp())) {
                    return -1;
                } else {
                    return 0;
                }
            }).collect(Collectors.toList());
        if (list.isEmpty()) {
            return new ArrayList<>();
        } else {
            MachineInfo machine = list.get(0);
            return sentinelApiClient.fetchFlowRuleOfMachine(machine.getApp(), machine.getIp(), machine.getPort());
        }
    }
}
