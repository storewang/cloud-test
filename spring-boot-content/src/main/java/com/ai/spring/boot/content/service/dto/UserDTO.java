package com.ai.spring.boot.content.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户数据类
 *
 * @author 石头
 * @Date 2020/3/25
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
public class UserDTO {
    private Long userId;
    private String userName;
    private Integer age;
    private String wxId;
    private String roles;
}
