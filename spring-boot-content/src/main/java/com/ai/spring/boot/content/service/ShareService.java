package com.ai.spring.boot.content.service;

import com.ai.spring.boot.content.convert.ShareDTOConvert;
import com.ai.spring.boot.content.dao.ShareDao;
import com.ai.spring.boot.content.dao.bean.Share;
import com.ai.spring.boot.content.feignclient.UserServiceFeignClient;
import com.ai.spring.boot.content.service.dto.ShareDTO;
import com.ai.spring.boot.content.service.dto.UserDTO;
import com.ai.spring.boot.content.util.JacksonJsonParser;
import com.ai.spring.im.common.bean.Response;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * 分享服务
 *
 * @author 石头
 * @Date 2020/3/25
 * @Version 1.0
 **/
@Service
@Slf4j
public class ShareService {
    @Autowired
    private ShareDao shareDao;
    @Autowired
    private ShareDTOConvert convert;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private UserServiceFeignClient userServiceFeignClient;

    /**
     * 没有测试出效果
     */
    @SentinelResource("common")
    public void testSentinel(){
        log.info("------testSentinel调用了-------");
    }

    public ShareDTO findById(Long shareId){
        Share share = shareDao.selectByPrimaryKey(shareId);
        /** UserDTO userDTO = getUserFromRemote(share.getUserId());*/
        /** UserDTO userDTO = getUserFromRemoteByBalanced(share.getUserId());*/
        UserDTO userDTO = getUserFromRemoteByFeign(share.getUserId());

        ShareDTO shareDTO = convert.toDto(share);
        shareDTO.setUserName(userDTO.getUserName());
        shareDTO.setWxId(userDTO.getWxId());
        return shareDTO;
    }

    /**
     * feign 客户端已经具在负载均衡的能力了
     * @param userId
     * @return
     */
    private UserDTO getUserFromRemoteByFeign(Long userId){
        Response<UserDTO> userResponse = userServiceFeignClient.findById(userId);
        return userResponse.getData();
    }

    /**
     * 负载均衡在restTemplate上已经配置了
     * <pre>String targetUrl = "http://spring-boot-user/users/{id}"</pre>
     * 代码不可读
     * url复杂，难以维护
     * 难以相应需求变化
     * 编程体验不统一
     * @param userId
     * @return
     */
    private UserDTO getUserFromRemoteByBalanced(Long userId){
        String targetUrl = "http://spring-boot-user/users/{id}";
        String userJson = restTemplate.getForObject(targetUrl, String.class, userId);
        log.info("请求目的地址：{}",targetUrl);
        JacksonJsonParser parser = new JacksonJsonParser();
        Response<UserDTO> userDTOResponse = parser.parseUserDTO(userJson);

        return userDTOResponse.getData();
    }

    /**
     * 手动获取target地址进行调用
     * @param userId
     * @return
     */
    private UserDTO getUserFromRemote(Long userId){
        List<ServiceInstance> instances = discoveryClient.getInstances("spring-boot-user");
        String targetUrl = instances.stream().map(instance -> instance.getUri().toString() + "/users/{id}").findFirst().orElseThrow(() -> new IllegalArgumentException("没有找到用户服务实例"));
        String userJson = restTemplate.getForObject(targetUrl, String.class, userId);
        /**
        String userJson = restTemplate.getForObject("http://localhost:8888/users/{id}", String.class, userId);*/
        log.info("请求目的地址：{}",targetUrl);
        JacksonJsonParser parser = new JacksonJsonParser();
        Response<UserDTO> userDTOResponse = parser.parseUserDTO(userJson);

        return userDTOResponse.getData();
    }
}
