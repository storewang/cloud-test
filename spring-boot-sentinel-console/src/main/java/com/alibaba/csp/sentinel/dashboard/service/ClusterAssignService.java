package com.alibaba.csp.sentinel.dashboard.service;

import com.alibaba.csp.sentinel.dashboard.vo.cluster.ClusterAppAssignResultVO;
import com.alibaba.csp.sentinel.dashboard.vo.cluster.request.ClusterAppAssignMap;

import java.util.List;
import java.util.Set;

/**
 *
 * @author 石头
 * @Date 2020/4/26
 * @Version 1.0
 **/
public interface ClusterAssignService {
    /**
     * Unbind a specific cluster server and its clients.
     *
     * @param app app name
     * @param machineId valid machine ID ({@code host@commandPort})
     * @return assign result
     */
    ClusterAppAssignResultVO unbindClusterServer(String app, String machineId);

    /**
     * Unbind a set of cluster servers and its clients.
     *
     * @param app app name
     * @param machineIdSet set of valid machine ID ({@code host@commandPort})
     * @return assign result
     */
    ClusterAppAssignResultVO unbindClusterServers(String app, Set<String> machineIdSet);

    /**
     * Apply cluster server and client assignment for provided app.
     *
     * @param app app name
     * @param clusterMap cluster assign map (server -> clients)
     * @param remainingSet unassigned set of machine ID
     * @return assign result
     */
    ClusterAppAssignResultVO applyAssignToApp(String app, List<ClusterAppAssignMap> clusterMap,
                                              Set<String> remainingSet);
}
