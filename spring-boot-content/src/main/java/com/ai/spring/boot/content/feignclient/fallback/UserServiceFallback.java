package com.ai.spring.boot.content.feignclient.fallback;

import com.ai.spring.boot.content.feignclient.UserServiceFeignClient;
import com.ai.spring.boot.content.service.dto.UserDTO;
import com.ai.spring.im.common.bean.Response;
import com.ai.spring.im.common.util.ResponseBuildUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 用户服务降级处理
 *
 * @author 石头
 * @Date 2020/3/27
 * @Version 1.0
 **/
@Component
public class UserServiceFallback implements UserServiceFeignClient{
    @Override
    public Response<UserDTO> findById(@PathVariable("id") Long userId) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userId);
        userDTO.setUserName("降级了");
        return ResponseBuildUtil.buildSuccess(userDTO);
    }
}
