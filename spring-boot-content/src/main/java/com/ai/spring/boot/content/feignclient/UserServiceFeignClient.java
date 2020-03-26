package com.ai.spring.boot.content.feignclient;

import com.ai.spring.boot.content.service.dto.UserDTO;
import com.ai.spring.im.common.bean.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 服务服务feign客户端
 *
 * @author 石头
 * @Date 2020/3/26
 * @Version 1.0
 **/
// 代码方式配置feign
//@FeignClient(name = "spring-boot-user",configuration = UserServiceFeignConf.class)
@FeignClient(name = "spring-boot-user")
public interface UserServiceFeignClient {
    @GetMapping("/users/{id}")
    Response<UserDTO> findById(@PathVariable("id")Long userId);
}
