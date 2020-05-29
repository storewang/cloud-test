package com.ai.spring.boot.flux.ws.mapping;

import com.ai.spring.boot.flux.ws.annotion.WebSoketMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * WebSocketMapping
 *
 * @author 石头
 * @Date 2020/5/28
 * @Version 1.0
 **/
@Slf4j
public class WebSocketHandlerMapping extends SimpleUrlHandlerMapping{
    private Map<String,WebSocketHandler> handlerMap = new LinkedHashMap<>(16);

    @Override
    public void initApplicationContext() throws BeansException {
        Map<String, Object> beansMap = obtainApplicationContext().getBeansWithAnnotation(WebSoketMapping.class);
        beansMap.values().stream().forEach(bean -> {
            if (! (bean instanceof WebSocketHandler)){
                throw new RuntimeException(String.format("Controller [%s] doesn't implement WebSocketeHandler interface.",bean.getClass().getName()));
            }
            WebSoketMapping webSoketMapping = AnnotationUtils.getAnnotation(bean.getClass(), WebSoketMapping.class);
            // 添加映射
            handlerMap.put(Objects.requireNonNull(webSoketMapping).value(),(WebSocketHandler) bean);
        });

        super.setOrder(Ordered.HIGHEST_PRECEDENCE);
        super.setUrlMap(handlerMap);
        super.initApplicationContext();
    }
}
