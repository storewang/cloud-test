package com.alibaba.csp.sentinel.dashboard.util;

/**
 * Sentinel Api 常量
 *
 * @author 石头
 * @Date 2020/4/26
 * @Version 1.0
 **/
public final class SentinelApiConstant {
    public static final String RESOURCE_URL_PATH = "jsonTree";
    public static final String CLUSTER_NODE_PATH = "clusterNode";
    public static final String GET_RULES_PATH = "getRules";
    public static final String SET_RULES_PATH = "setRules";
    public static final String GET_PARAM_RULE_PATH = "getParamFlowRules";
    public static final String SET_PARAM_RULE_PATH = "setParamFlowRules";

    public static final String FETCH_CLUSTER_MODE_PATH = "getClusterMode";
    public static final String MODIFY_CLUSTER_MODE_PATH = "setClusterMode";
    public static final String FETCH_CLUSTER_CLIENT_CONFIG_PATH = "cluster/client/fetchConfig";
    public static final String MODIFY_CLUSTER_CLIENT_CONFIG_PATH = "cluster/client/modifyConfig";

    public static final String FETCH_CLUSTER_SERVER_ALL_CONFIG_PATH = "cluster/server/fetchConfig";
    public static final String FETCH_CLUSTER_SERVER_BASIC_INFO_PATH = "cluster/server/info";

    public static final String MODIFY_CLUSTER_SERVER_TRANSPORT_CONFIG_PATH = "cluster/server/modifyTransportConfig";
    public static final String MODIFY_CLUSTER_SERVER_FLOW_CONFIG_PATH = "cluster/server/modifyFlowConfig";
    public static final String MODIFY_CLUSTER_SERVER_NAMESPACE_SET_PATH = "cluster/server/modifyNamespaceSet";

    public static final String FETCH_GATEWAY_API_PATH = "gateway/getApiDefinitions";
    public static final String MODIFY_GATEWAY_API_PATH = "gateway/updateApiDefinitions";

    public static final String FETCH_GATEWAY_FLOW_RULE_PATH = "gateway/getRules";
    public static final String MODIFY_GATEWAY_FLOW_RULE_PATH = "gateway/updateRules";

    public static final String FLOW_RULE_TYPE = "flow";
    public static final String DEGRADE_RULE_TYPE = "degrade";
    public static final String SYSTEM_RULE_TYPE = "system";
    public static final String AUTHORITY_TYPE = "authority";
}
