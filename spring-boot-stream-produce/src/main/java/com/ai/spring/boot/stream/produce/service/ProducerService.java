package com.ai.spring.boot.stream.produce.service;

import com.ai.spring.boot.stream.produce.dao.SendDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消息发送服务
 *
 * @author 石头
 * @Date 2020/5/27
 * @Version 1.0
 **/
@RestController
public class ProducerService {
    @Autowired
    private SendDao sendDao;

    @RequestMapping("/send")
    public void send(@RequestParam("msg") String msg){
        sendDao.sendMsg(msg);
    }
}
