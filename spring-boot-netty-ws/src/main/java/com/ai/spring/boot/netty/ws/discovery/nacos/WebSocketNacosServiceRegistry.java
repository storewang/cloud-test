package com.ai.spring.boot.netty.ws.discovery.nacos;

import com.ai.spring.boot.netty.ws.conf.ServerProperties;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * websocket服务注册
 *
 * @author 石头
 * @Date 2021/1/8
 * @Version 1.0
 **/
@Slf4j
public class WebSocketNacosServiceRegistry implements ServiceRegistry<Registration> {
    private final String SERVICENAME_SUB = "-ws";
    private final NacosDiscoveryProperties nacosDiscoveryProperties;
    private final ServerProperties serverProperties;
    private final NamingService namingService;
    public WebSocketNacosServiceRegistry(NacosDiscoveryProperties nacosDiscoveryProperties,ServerProperties serverProperties) {
        this.nacosDiscoveryProperties = nacosDiscoveryProperties;
        this.namingService = nacosDiscoveryProperties.namingServiceInstance();
        this.serverProperties = serverProperties;
    }

    private String getSocketServiceId(Registration registration){
        String serviceId = registration.getServiceId();
        return serviceId + SERVICENAME_SUB;
    }
    private Instance getNacosInstanceFromRegistration(Registration registration) {
        Instance instance = new Instance();
        instance.setIp(serverProperties.getHost());
        instance.setPort(serverProperties.getPort());
        instance.setWeight(nacosDiscoveryProperties.getWeight());
        instance.setClusterName(nacosDiscoveryProperties.getClusterName());
        instance.setMetadata(registration.getMetadata());

        return instance;
    }

    @Override
    public void register(Registration registration) {
        // 依赖于服务，所以判断服务ID
        if (StringUtils.isEmpty(registration.getServiceId())) {
            log.warn("No websocket service to register for nacos client...");
            return;
        }
        String serviceId = getSocketServiceId(registration);
        String group = nacosDiscoveryProperties.getGroup();
        log.info("---------------register websocket {}-{} starting...----------------------",serviceId,group);

        Instance instance = getNacosInstanceFromRegistration(registration);
        try {
            namingService.registerInstance(serviceId, group, instance);
            log.info("-----nacos registry, {} {} {}:{} register finished", group, serviceId,
                    instance.getIp(), instance.getPort());
        }catch (Exception e){
            log.error("nacos registry, {} register failed...{},", serviceId,
                    registration.toString(), e);
        }
    }

    @Override
    public void deregister(Registration registration) {
        log.info("De-registering from Nacos Server now...");

        if (StringUtils.isEmpty(registration.getServiceId())) {
            log.warn("No dom to de-register for nacos client...");
            return;
        }

        String serviceId = getSocketServiceId(registration);
        String group = nacosDiscoveryProperties.getGroup();
        try {
            namingService.deregisterInstance(serviceId, group, serverProperties.getHost(),
                    serverProperties.getPort(), nacosDiscoveryProperties.getClusterName());

        }catch (Exception e){
            log.error("ERR_NACOS_DEREGISTER, de-register failed...{},",
                    registration.toString(), e);
        }
    }

    @Override
    public void close() {

    }

    @Override
    public void setStatus(Registration registration, String status) {
        if (!status.equalsIgnoreCase("UP") && !status.equalsIgnoreCase("DOWN")) {
            log.warn("can't support status {},please choose UP or DOWN", status);
            return;
        }

        String serviceId = getSocketServiceId(registration);
        Instance instance = getNacosInstanceFromRegistration(registration);
        if (status.equalsIgnoreCase("DOWN")) {
            instance.setEnabled(false);
        }else {
            instance.setEnabled(true);
        }

        try {
            nacosDiscoveryProperties.namingMaintainServiceInstance()
                    .updateInstance(serviceId, instance);

        }catch (Exception e){
            throw new RuntimeException("update nacos instance status fail", e);
        }
    }

    @Override
    public Object getStatus(Registration registration) {
        String serviceName = getSocketServiceId(registration);
        try{
            List<Instance> instances = nacosDiscoveryProperties.namingServiceInstance().getAllInstances(serviceName);
            for (Instance instance : instances) {
                if (instance.getIp().equalsIgnoreCase(serverProperties.getHost())
                        && instance.getPort() == serverProperties.getPort()){
                    return instance.isEnabled() ? "UP" : "DOWN";
                }
            }
        }catch (Exception e){
            log.error("get all instance of {} error,", serviceName, e);
        }
        return null;
    }
}
