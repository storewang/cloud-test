package com.ai.spring.boot.im.dto;

import lombok.Data;
import lombok.ToString;

/**
 * TODO
 *
 * @author 石头
 * @Date 2020/3/11
 * @Version 1.0
 **/
@Data
@ToString
public class ShellImageDTO {
    private String errorCode;
    private String msg;
    private ShellImageBody data;
}
