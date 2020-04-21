package com.ai.spring.boot.im.controller;

import com.ai.spring.boot.im.dto.NotifyHouseInfo;
import com.ai.spring.boot.im.dto.NotifyInfoData;
import com.ai.spring.boot.im.dto.PingNotifyInfoData;
import com.ai.spring.boot.im.dto.ShellHouseDTO;
import com.ai.spring.boot.im.dto.ShellImageDTO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;

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

    @PostMapping("/notifyCallBack/{landLord}")
    public String notifyCallBack(@PathVariable("landLord") String landlord, @RequestParam String sign, @RequestBody String houseDTO){
        log.info("---{} call back house : {},{} ",landlord,sign,houseDTO);

        NotifyInfoData notifyInfoData = getNotifyCallBackInfo(sign, houseDTO);
        log.info("---{} call back notifyInfoData : {} ",landlord,notifyInfoData);
        return "success";
    }

    @GetMapping("/")
    public String home(){
        log.info("---welcome to spring boot home.--");
        return "hello world!";
    }

    private NotifyInfoData getNotifyCallBackInfo(String sign,String callbackStr){
        try {
            String decode = URLDecoder.decode(callbackStr, "UTF-8");
            if (decode.indexOf(sign)>-1){
                String callBackInfo = decode.substring(decode.indexOf(sign)+sign.length());
                callBackInfo = callBackInfo.substring(1,callBackInfo.indexOf("="));

                FastJsonConfig config = new FastJsonConfig();
                config.setSerializerFeatures(SerializerFeature.PrettyFormat);

                PingNotifyInfoData pingNotifyInfoData = JSON.parseObject(callBackInfo, PingNotifyInfoData.class, config.getFeatures());

                return pingNotifyInfoData;
            }
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
