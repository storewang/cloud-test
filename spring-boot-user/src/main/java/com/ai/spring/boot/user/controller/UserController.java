package com.ai.spring.boot.user.controller;

import com.ai.spring.boot.user.service.dto.UserDTO;
import com.ai.spring.boot.user.service.user.UserService;
import com.ai.spring.im.common.bean.Response;
import com.ai.spring.im.common.util.ResponseBuildUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户对外接口
 *
 * @author 石头
 * @Date 2020/3/25
 * @Version 1.0
 **/
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public Response<UserDTO> findById(@PathVariable("id")Long userId){
        UserDTO userDTO = userService.findById(userId);
        return ResponseBuildUtil.buildSuccess(userDTO);
    }
}
