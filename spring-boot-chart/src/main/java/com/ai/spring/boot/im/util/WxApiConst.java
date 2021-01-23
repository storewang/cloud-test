package com.ai.spring.boot.im.util;

/**
 * Created by 石头 on 2020/9/13.
 */
public final class WxApiConst {
    public static final String accessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
    public static final String userInfoUrl = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN";
}
