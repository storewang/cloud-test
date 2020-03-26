package com.ai.spring.boot.content.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分享信息
 *
 * @author 石头
 * @Date 2020/3/25
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
public class ShareDTO {
    private Long shareId;
    private String title;
    private Boolean isOriginal;
    private String author;
    private String conver;
    private String summary;
    private Integer price;
    private String auditStatus;

    private Long userId;
    private String userName;
    private String wxId;
}
