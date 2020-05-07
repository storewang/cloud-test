package com.alibaba.csp.sentinel.dashboard.conf;

import com.alibaba.csp.sentinel.adapter.servlet.CommonFilter;
import com.alibaba.csp.sentinel.dashboard.filter.AuthFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;

/**
 * TODO
 *
 * @author 石头
 * @Date 2020/5/6
 * @Version 1.0
 **/
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final Logger logger = LoggerFactory.getLogger(WebConfig.class);

    @Autowired
    private AuthFilter authFilter;

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/**").addResourceLocations("classpath:/resources/");
//    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.htm");
    }

    /**
     * Add {@link CommonFilter} to the server, this is the simplest way to use Sentinel
     * for Web application.
     */
    @Bean
    public FilterRegistrationBean sentinelFilterRegistration() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new CommonFilter());
        registration.addUrlPatterns("/*");
        registration.setName("sentinelFilter");
        registration.setOrder(1);

        logger.info("Sentinel servlet CommonFilter registered");

        return registration;
    }

    @Bean
    public FilterRegistrationBean authenticationFilterRegistration() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(authFilter);
        registration.addUrlPatterns("/*");
        registration.setName("authenticationFilter");
        registration.setOrder(0);
        return registration;
    }
}
