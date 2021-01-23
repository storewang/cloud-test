package com.ai.spring.boot.im.dto.wxchart;

import lombok.Data;

/**
 * Created by 石头 on 2020/9/13.
 */
@Data
public class AccessTokenDTO {
    private String access_token;
    private Long expires_in;
}
