package com.ai.spring.boot.gateway.pridicate.beans;

import lombok.Data;

import java.time.LocalTime;

/**
 * 时间段配置
 *
 * @author 石头
 * @Date 2020/3/30
 * @Version 1.0
 **/
@Data
public class TimeBetween {
    private LocalTime start;
    private LocalTime end;
}
