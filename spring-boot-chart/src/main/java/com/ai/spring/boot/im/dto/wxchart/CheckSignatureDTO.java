package com.ai.spring.boot.im.dto.wxchart;

import lombok.Data;

/**
 * 公众号接入验证DTO
 * Created by 石头 on 2020/9/12.
 */
@Data
public class CheckSignatureDTO {
    /**微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。*/
    private String signature;
    /**时间戳*/
    private Long timestamp;
    /**随机数*/
    private Long nonce;
    /**随机字符串*/
    private String echostr;
}
