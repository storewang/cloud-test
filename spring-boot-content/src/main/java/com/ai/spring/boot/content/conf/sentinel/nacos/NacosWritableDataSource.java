package com.ai.spring.boot.content.conf.sentinel.nacos;

import com.ai.spring.boot.content.conf.SentinelConf;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.datasource.WritableDataSource;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

/**
 * sentinel 挺久化写nacos实现
 *
 * @author 石头
 * @Date 2020/4/1
 * @Version 1.0
 **/
@Slf4j
public class NacosWritableDataSource<T> implements WritableDataSource<T> {
    private SentinelConf conf;
    private ConfigService configService;
    private Properties properties;
    private final Converter<T, String> configEncoder;

    public NacosWritableDataSource(SentinelConf conf,Converter<T, String> configEncoder){
        this.conf = conf;
        this.configEncoder = configEncoder;
        properties = buildProperties();

        initNacosConfigService();
    }
    @Override
    public void write(T value) throws Exception {
        try{
            String convertResult = configEncoder.convert(value);

            configService.publishConfig(conf.getDataId(),conf.getGroupId(),convertResult);
        }catch (Exception e){
            log.warn("[NacosDataSource] Error occurred when write config to Nacos",e);
        }
    }

    @Override
    public void close() throws Exception {

    }

    private void initNacosConfigService(){
        try {
            this.configService = NacosFactory.createConfigService(this.properties);
        }catch (Exception e){
            log.warn("[NacosDataSource] Error occurred when initializing Nacos data source",e);
        }
    }
    Properties buildProperties(){
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.SERVER_ADDR, conf.getNacosServer());
        return properties;
    }
}
