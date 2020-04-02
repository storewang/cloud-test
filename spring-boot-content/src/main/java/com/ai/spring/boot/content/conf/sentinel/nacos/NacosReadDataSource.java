package com.ai.spring.boot.content.conf.sentinel.nacos;

import com.ai.spring.boot.content.conf.SentinelConf;
import com.alibaba.csp.sentinel.concurrent.NamedThreadFactory;
import com.alibaba.csp.sentinel.datasource.AbstractDataSource;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.nacos.api.config.listener.Listener;

import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * sentinel 挺久化读nacos实现
 *
 * @author 石头
 * @Date 2020/4/1
 * @Version 1.0
 **/
@Slf4j
public class NacosReadDataSource<T> extends AbstractDataSource<String, T> {
    private final Listener configListener;
    private SentinelConf conf;
    /**单线程的线程池,一但线程满了，就把老的任务丢弃掉*/
    private final ExecutorService pool =
            new ThreadPoolExecutor(1, 1, 0,
                    TimeUnit.MILLISECONDS,
                    new ArrayBlockingQueue<Runnable>(1),
                    new NamedThreadFactory("sentinel-nacos-ds-update"),
                    new ThreadPoolExecutor.DiscardOldestPolicy());
    private ConfigService configService;
    private Properties properties;

    public NacosReadDataSource(SentinelConf conf, Converter<String, T> parser) {
        super(parser);
        this.conf = conf;
        properties = buildProperties();

        this.configListener = new Listener() {
            @Override
            public Executor getExecutor() {
                return pool;
            }

            @Override
            public void receiveConfigInfo(String configInfo) {
                log.info("[NacosDataSource] New property value received for (properties: {}) (dataId: {}, groupId: {}): {}",properties,conf.getDataId(),conf.getGroupId());
                T newValue = NacosReadDataSource.this.parser.convert(configInfo);
                //Update the new value to the property.
                getProperty().updateValue(newValue);
            }
        };

        initNacosListener();
        loadInitialConfig();
    }
    private void loadInitialConfig() {
        try {
            T newValue = loadConfig();
            if (newValue == null) {
                log.warn("[NacosDataSource] WARN: initial config is null, you may have to check your data source");
            }
            // Update the new value to the property.
            getProperty().updateValue(newValue);
        }catch (Exception e){
            log.warn("[NacosDataSource] Error when loading initial config",e);
        }
    }
    private void initNacosListener() {
        try {
            this.configService = NacosFactory.createConfigService(this.properties);
            //  Add config listener.
            configService.addListener(conf.getDataId(), conf.getGroupId(), configListener);
        }catch (Exception e){
            log.warn("[NacosDataSource] Error occurred when initializing Nacos data source",e);
        }
    }
    @Override
    public String readSource() throws Exception {
        if (configService == null) {
            throw new IllegalStateException("Nacos config service has not been initialized or error occurred");
        }
        return configService.getConfig(conf.getDataId(), conf.getGroupId(), conf.getReadTimeout());
    }

    @Override
    public void close() throws Exception {
        if (configService != null) {
            configService.removeListener(conf.getDataId(), conf.getGroupId(), configListener);
        }
        pool.shutdownNow();
    }

    Properties buildProperties(){
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.SERVER_ADDR, conf.getNacosServer());
        return properties;
    }
}
