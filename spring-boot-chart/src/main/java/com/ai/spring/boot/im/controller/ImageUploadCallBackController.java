package com.ai.spring.boot.im.controller;

import com.ai.spring.boot.im.dto.ShellHouseDTO;
import com.ai.spring.boot.im.dto.ShellImageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * 贝壳图片上传回调测试
 *
 * @author 石头
 * @Date 2020/3/11
 * @Version 1.0
 **/
@RestController
@Slf4j
public class ImageUploadCallBackController {
    @Autowired
    private RestTemplate restTemplate;
    @PostMapping("/callBack/{landLord}")
    public void imageCallBack(@PathVariable("landLord") String landlord, @RequestBody ShellImageDTO imageDTO){
        log.info("---{} call back image : {}--",landlord,imageDTO);

        try {
            restTemplate.postForObject(new URI("https://hb.mgzf.com/partnerpc-flat/dockingcenter/callback/image/"+landlord),imageDTO,ShellImageDTO.class);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/houseCallBack/{landLord}")
    public void houseCallBack(@PathVariable("landLord") String landlord,@RequestBody ShellHouseDTO houseDTO){
        log.info("---{} call back house : {} {}--",landlord,houseDTO,houseDTO.getEventData().getClass());
        String eventType = houseDTO.getEventType();
        try {
//            if (eventType.equals("200") || eventType.equals("300")){
                restTemplate.postForObject(new URI("https://hb.mgzf.com/partnerpc-flat/dockingcenter/callback/house/"+landlord),houseDTO,ShellHouseDTO.class);
//            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/")
    public String home(){
        log.info("---welcome to spring boot home.--");
        return "hello world!";
    }
}
