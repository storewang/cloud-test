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
public class ShellImageBody {
    private Long id;
    private String imageUrl;
    private String path;
}
