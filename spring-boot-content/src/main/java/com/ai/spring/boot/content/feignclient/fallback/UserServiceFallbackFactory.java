package com.ai.spring.boot.content.feignclient.fallback;

import com.ai.spring.boot.content.feignclient.UserServiceFeignClient;
import com.ai.spring.boot.content.service.dto.UserDTO;
import com.ai.spring.im.common.bean.Response;
import com.ai.spring.im.common.util.ResponseBuildUtil;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 用户服务降级处理,可以捕获异常
 *
 * @author 石头
 * @Date 2020/3/27
 * @Version 1.0
 **/
@Component
@Slf4j
public class UserServiceFallbackFactory implements FallbackFactory<UserServiceFeignClient>{
    @Override
    public UserServiceFeignClient create(Throwable throwable) {
        return (userId) -> {
            log.warn("远程调用被限流/降级了",throwable);

            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(userId);
            userDTO.setUserName("降级了");
            return ResponseBuildUtil.buildSuccess(userDTO);
        };
    }
}
