package com.ai.spring.boot.netty.ws.util;

import com.ai.spring.boot.netty.ws.model.UserDTO;
import com.ai.spring.im.common.util.EncryptUtils;

/**
 * user code 绑助类
 *
 * @author 石头
 * @Date 2020/6/30
 * @Version 1.0
 **/
public final class UserCodeUtil {
    public static String getUserCode(String token, String channelId,UserDTO userDTO){
        String utoken = token + Consts.STR_SPLIT + userDTO.getUserId() + Consts.STR_SPLIT + channelId;

        return EncryptUtils.encrypt(utoken,EncryptUtils.DEF_PWD,EncryptUtils.SALT);
    }

    public static String getTokenByUserCode(String userCode){
        String utoken = EncryptUtils.decrypt(userCode,EncryptUtils.DEF_PWD,EncryptUtils.SALT);
        return utoken;
    }
}
