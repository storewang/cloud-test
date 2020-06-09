package com.ai.spring.boot.ds.conf;

import com.ai.spring.boot.ds.annotation.EnableJpaDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.domain.EntityScanPackages;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.data.jpa.repository.config.JpaRepositoryConfigExtension;
import org.springframework.data.repository.config.AnnotationRepositoryConfigurationSource;
import org.springframework.data.repository.config.RepositoryConfigurationDelegate;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;
import org.springframework.data.repository.config.RepositoryConfigurationUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 配置条件选择器
 *
 * @author 石头
 * @Date 2020/6/8
 * @Version 1.0
 **/
@Slf4j
public class DatasourceConfigurationRegistrar implements ImportSelector,ImportBeanDefinitionRegistrar,ResourceLoaderAware, EnvironmentAware {
    private ResourceLoader resourceLoader;
    private Environment environment;
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{JpaDatasourceConfiguration.class.getName()};
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        // 扫描注册entity
        register(registry, getPackagesToScan(metadata));

        // 扫描注册JpaRepository
        registerJpaRepository(metadata,registry);
    }

    private void registerJpaRepository(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry){
        AnnotationRepositoryConfigurationSource configurationSource = new AnnotationRepositoryConfigurationSource(annotationMetadata, EnableJpaDataSource.class, this.resourceLoader, this.environment, registry);
        RepositoryConfigurationExtension extension = new JpaRepositoryConfigExtension();
        RepositoryConfigurationUtils.exposeRegistration(extension, registry, configurationSource);
        RepositoryConfigurationDelegate delegate = new RepositoryConfigurationDelegate(configurationSource, this.resourceLoader, this.environment);
        delegate.registerRepositoriesIn(registry, extension);
    }

    private void register(BeanDefinitionRegistry registry,
                                Collection<String> packageNames) {
        EntityScanPackages.register(registry,packageNames);
    }

    private Set<String> getPackagesToScan(AnnotationMetadata metadata) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(
                metadata.getAnnotationAttributes(EnableJpaDataSource.class.getName()));
        String[] basePackages = attributes.getStringArray("entityBasePackages");
        Set<String> packagesToScan = new LinkedHashSet<String>();
        packagesToScan.addAll(Arrays.asList(basePackages));
        if (packagesToScan.isEmpty()) {
            String packageName = ClassUtils.getPackageName(metadata.getClassName());
            Assert.state(!StringUtils.isEmpty(packageName),
                    "@EntityScan cannot be used with the default package");
            return Collections.singleton(packageName);
        }
        return packagesToScan;
    }
}
