package com.ai.spring.boot.stream.consumer.conf;

import com.ai.spring.boot.stream.consumer.service.RecieveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

/**
 * 应用启动后会触发
 *
 * @author 石头
 * @Date 2020/5/27
 * @Version 1.0
 **/
@Component
public class ApplicationRunner implements org.springframework.boot.ApplicationRunner{
    @Autowired
    private RecieveService recieveService;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        recieveService.recieve("stream-demo1");
    }
}
