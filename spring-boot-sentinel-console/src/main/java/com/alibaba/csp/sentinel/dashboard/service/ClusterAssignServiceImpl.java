package com.alibaba.csp.sentinel.dashboard.service;

import com.alibaba.csp.sentinel.cluster.ClusterStateManager;
import com.alibaba.csp.sentinel.dashboard.dao.entity.cluster.ClusterGroupEntity;
import com.alibaba.csp.sentinel.dashboard.service.client.SentinelApiClient;
import com.alibaba.csp.sentinel.dashboard.util.MachineUtils;
import com.alibaba.csp.sentinel.dashboard.vo.cluster.ClusterAppAssignResultVO;
import com.alibaba.csp.sentinel.dashboard.vo.cluster.conf.ClusterClientConfig;
import com.alibaba.csp.sentinel.dashboard.vo.cluster.conf.ServerFlowConfig;
import com.alibaba.csp.sentinel.dashboard.vo.cluster.conf.ServerTransportConfig;
import com.alibaba.csp.sentinel.dashboard.vo.cluster.request.ClusterAppAssignMap;
import com.alibaba.csp.sentinel.dashboard.vo.cluster.state.ClusterUniversalStatePairVO;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.alibaba.csp.sentinel.util.function.Tuple2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * ClusterAssignService
 *
 * @author 石头
 * @Date 2020/4/26
 * @Version 1.0
 **/
@Service
@Slf4j
public class ClusterAssignServiceImpl implements ClusterAssignService{
    @Autowired
    private SentinelApiClient sentinelApiClient;
    @Autowired
    private ClusterConfigService clusterConfigService;

    @Override
    public ClusterAppAssignResultVO unbindClusterServer(String app, String machineId) {
        AssertUtil.assertNotBlank(app, "app cannot be blank");
        AssertUtil.assertNotBlank(machineId, "machineId cannot be blank");

        if (isMachineInApp(machineId)) {
            return handleUnbindClusterServerNotInApp(app, machineId);
        }
        Set<String> failedSet = new HashSet<>();
        try {
            ClusterGroupEntity entity = clusterConfigService.getClusterUniversalStateForAppMachine(app, machineId)
                    .get(10, TimeUnit.SECONDS);
            Set<String> toModifySet = new HashSet<>();
            toModifySet.add(machineId);
            if (entity.getClientSet() != null) {
                toModifySet.addAll(entity.getClientSet());
            }
            // Modify mode to NOT-STARTED for all chosen token servers and associated token clients.
            modifyToNonStarted(toModifySet, failedSet);
        } catch (Exception ex) {
            Throwable e = ex instanceof ExecutionException ? ex.getCause() : ex;
            log.error("Failed to unbind machine <{}>", machineId, e);
            failedSet.add(machineId);
        }
        return new ClusterAppAssignResultVO()
                .setFailedClientSet(failedSet)
                .setFailedServerSet(new HashSet<>());
    }
    @Override
    public ClusterAppAssignResultVO unbindClusterServers(String app, Set<String> machineIdSet) {
        AssertUtil.assertNotBlank(app, "app cannot be blank");
        AssertUtil.isTrue(machineIdSet != null && !machineIdSet.isEmpty(), "machineIdSet cannot be empty");
        ClusterAppAssignResultVO result = new ClusterAppAssignResultVO()
                .setFailedClientSet(new HashSet<>())
                .setFailedServerSet(new HashSet<>());
        for (String machineId : machineIdSet) {
            ClusterAppAssignResultVO resultVO = unbindClusterServer(app, machineId);
            result.getFailedClientSet().addAll(resultVO.getFailedClientSet());
            result.getFailedServerSet().addAll(resultVO.getFailedServerSet());
        }
        return result;
    }
    @Override
    public ClusterAppAssignResultVO applyAssignToApp(String app, List<ClusterAppAssignMap> clusterMap,Set<String> remainingSet) {
        AssertUtil.assertNotBlank(app, "app cannot be blank");
        AssertUtil.notNull(clusterMap, "clusterMap cannot be null");
        Set<String> failedServerSet = new HashSet<>();
        Set<String> failedClientSet = new HashSet<>();

        // Assign server and apply config.
        clusterMap.stream()
                .filter(Objects::nonNull)
                .filter(ClusterAppAssignMap::getBelongToApp)
                .map(e -> {
                    String ip = e.getIp();
                    int commandPort = parsePort(e);
                    CompletableFuture<Void> f = modifyMode(ip, commandPort, ClusterStateManager.CLUSTER_SERVER)
                            .thenCompose(v -> applyServerConfigChange(app, ip, commandPort, e));
                    return Tuple2.of(e.getMachineId(), f);
                })
                .forEach(t -> handleFutureSync(t, failedServerSet));

        // Assign client of servers and apply config.
        clusterMap.parallelStream()
                .filter(Objects::nonNull)
                .forEach(e -> applyAllClientConfigChange(app, e, failedClientSet));

        // Unbind remaining (unassigned) machines.
        applyAllRemainingMachineSet(app, remainingSet, failedClientSet);

        return new ClusterAppAssignResultVO()
                .setFailedClientSet(failedClientSet)
                .setFailedServerSet(failedServerSet);
    }
    private void applyAllRemainingMachineSet(String app, Set<String> remainingSet, Set<String> failedSet) {
        if (remainingSet == null || remainingSet.isEmpty()) {
            return;
        }
        remainingSet.parallelStream()
                .filter(Objects::nonNull)
                .map(MachineUtils::parseCommandIpAndPort)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(ipPort -> {
                    String ip = ipPort.r1;
                    int commandPort = ipPort.r2;
                    CompletableFuture<Void> f = modifyMode(ip, commandPort, ClusterStateManager.CLUSTER_NOT_STARTED);
                    return Tuple2.of(ip + '@' + commandPort, f);
                })
                .forEach(t -> handleFutureSync(t, failedSet));
    }
    private void applyAllClientConfigChange(String app, ClusterAppAssignMap assignMap,
                                            Set<String> failedSet) {
        Set<String> clientSet = assignMap.getClientSet();
        if (clientSet == null || clientSet.isEmpty()) {
            return;
        }
        final String serverIp = assignMap.getIp();
        final int serverPort = assignMap.getPort();
        clientSet.stream()
                .map(MachineUtils::parseCommandIpAndPort)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(ipPort -> {
                    CompletableFuture<Void> f = sentinelApiClient
                            .modifyClusterMode(ipPort.r1, ipPort.r2, ClusterStateManager.CLUSTER_CLIENT)
                            .thenCompose(v -> sentinelApiClient.modifyClusterClientConfig(app, ipPort.r1, ipPort.r2,
                                    new ClusterClientConfig().setRequestTimeout(20)
                                            .setServerHost(serverIp)
                                            .setServerPort(serverPort)
                            ));
                    return Tuple2.of(ipPort.r1 + '@' + ipPort.r2, f);
                })
                .forEach(t -> handleFutureSync(t, failedSet));
    }
    private CompletableFuture<Void> applyServerConfigChange(String app, String ip, int commandPort,
                                                            ClusterAppAssignMap assignMap) {
        ServerTransportConfig transportConfig = new ServerTransportConfig()
                .setPort(assignMap.getPort())
                .setIdleSeconds(600);
        return sentinelApiClient.modifyClusterServerTransportConfig(app, ip, commandPort, transportConfig)
                .thenCompose(v -> applyServerFlowConfigChange(app, ip, commandPort, assignMap))
                .thenCompose(v -> applyServerNamespaceSetConfig(app, ip, commandPort, assignMap));
    }
    private CompletableFuture<Void> applyServerFlowConfigChange(String app, String ip, int commandPort,
                                                                ClusterAppAssignMap assignMap) {
        Double maxAllowedQps = assignMap.getMaxAllowedQps();
        if (maxAllowedQps == null || maxAllowedQps <= 0 || maxAllowedQps > 20_0000) {
            return CompletableFuture.completedFuture(null);
        }
        return sentinelApiClient.modifyClusterServerFlowConfig(app, ip, commandPort,
                new ServerFlowConfig().setMaxAllowedQps(maxAllowedQps));
    }
    private CompletableFuture<Void> applyServerNamespaceSetConfig(String app, String ip, int commandPort,
                                                                  ClusterAppAssignMap assignMap) {
        Set<String> namespaceSet = assignMap.getNamespaceSet();
        if (namespaceSet == null || namespaceSet.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }
        return sentinelApiClient.modifyClusterServerNamespaceSet(app, ip, commandPort, namespaceSet);
    }

    private boolean isMachineInApp(/*@NonEmpty*/ String machineId) {
        return machineId.contains(":");
    }
    private ClusterAppAssignResultVO handleUnbindClusterServerNotInApp(String app, String machineId) {
        Set<String> failedSet = new HashSet<>();
        try {
            List<ClusterUniversalStatePairVO> list = clusterConfigService.getClusterUniversalState(app)
                    .get(10, TimeUnit.SECONDS);
            Set<String> toModifySet = list.stream()
                    .filter(e -> e.getState().getStateInfo().getMode() == ClusterStateManager.CLUSTER_CLIENT)
                    .filter(e -> machineId.equals(e.getState().getClient().getClientConfig().getServerHost() + ':' +
                            e.getState().getClient().getClientConfig().getServerPort()))
                    .map(e -> e.getIp() + '@' + e.getCommandPort())
                    .collect(Collectors.toSet());
            // Modify mode to NOT-STARTED for all associated token clients.
            modifyToNonStarted(toModifySet, failedSet);
        } catch (Exception ex) {
            Throwable e = ex instanceof ExecutionException ? ex.getCause() : ex;
            log.error("Failed to unbind machine <{}>", machineId, e);
            failedSet.add(machineId);
        }
        return new ClusterAppAssignResultVO()
                .setFailedClientSet(failedSet)
                .setFailedServerSet(new HashSet<>());
    }
    private void modifyToNonStarted(Set<String> toModifySet, Set<String> failedSet) {
        toModifySet.parallelStream()
                .map(MachineUtils::parseCommandIpAndPort)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(e -> {
                    CompletableFuture<Void> f = modifyMode(e.r1, e.r2, ClusterStateManager.CLUSTER_NOT_STARTED);
                    return Tuple2.of(e.r1 + '@' + e.r2, f);
                })
                .forEach(f -> handleFutureSync(f, failedSet));
    }

    private CompletableFuture<Void> modifyMode(String ip, int port, int mode) {
        return sentinelApiClient.modifyClusterMode(ip, port, mode);
    }

    private void handleFutureSync(Tuple2<String, CompletableFuture<Void>> t, Set<String> failedSet) {
        try {
            t.r2.get(10, TimeUnit.SECONDS);
        } catch (Exception ex) {
            if (ex instanceof ExecutionException) {
                log.error("Request for <{}> failed", t.r1, ex.getCause());
            } else {
                log.error("Request for <{}> failed", t.r1, ex);
            }
            failedSet.add(t.r1);
        }
    }
    private int parsePort(ClusterAppAssignMap assignMap) {
        return MachineUtils.parseCommandPort(assignMap.getMachineId())
                .orElse(ServerTransportConfig.DEFAULT_PORT);
    }
}
