package com.ai.spring.boot.netty.ws.discovery.nacos;

import com.alibaba.cloud.nacos.registry.NacosRegistration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.serviceregistry.AbstractAutoServiceRegistration;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.util.Assert;

/**
 * websocket服务自动注册
 *
 * @author 石头
 * @Date 2021/1/8
 * @Version 1.0
 **/
@Slf4j
public class WebSocketNacosAutoServiceRegistration extends AbstractAutoServiceRegistration<Registration> {
    private NacosRegistration registration;

    public WebSocketNacosAutoServiceRegistration(ServiceRegistry<Registration> serviceRegistry,
                                        AutoServiceRegistrationProperties autoServiceRegistrationProperties,
                                        NacosRegistration registration) {
        super(serviceRegistry, autoServiceRegistrationProperties);
        this.registration = registration;
    }

    @Override
    protected Object getConfiguration() {
        return this.registration.getNacosDiscoveryProperties();
    }

    @Override
    protected boolean isEnabled() {
        return this.registration.getNacosDiscoveryProperties().isRegisterEnabled();
    }

    @Override
    protected Registration getRegistration() {
        if (this.registration.getPort() < 0 && this.getPort().get() > 0) {
            this.registration.setPort(this.getPort().get());
        }
        Assert.isTrue(this.registration.getPort() > 0, "service.port has not been set");
        return this.registration;
    }

    @Override
    protected Registration getManagementRegistration() {
        return null;
    }

    @Deprecated
    public void setPort(int port) {
        getPort().set(port);
    }

    @Override
    protected void register() {
        log.info("---------------register websocket----------------------");
        if (!this.registration.getNacosDiscoveryProperties().isRegisterEnabled()) {
            log.debug("Registration disabled.");
            return;
        }
        if (this.registration.getPort() < 0) {
            this.registration.setPort(getPort().get());
        }
        super.register();
    }
}
