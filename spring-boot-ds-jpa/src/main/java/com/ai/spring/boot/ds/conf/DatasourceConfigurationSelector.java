package com.ai.spring.boot.ds.conf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 配置条件选择器
 *
 * @author 石头
 * @Date 2020/6/8
 * @Version 1.0
 **/
@Slf4j
public class DatasourceConfigurationSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{JpaDatasourceConfiguration.class.getName()};
    }
}
