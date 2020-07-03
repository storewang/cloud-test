package com.ai.spring.boot.netty.ws.util;

import com.ai.spring.boot.netty.ws.model.UserDTO;
import com.ai.spring.im.common.util.EncryptUtils;
import com.ai.spring.im.common.util.StringUtil;

import java.util.Optional;

/**
 * user code 绑助类
 *
 * @author 石头
 * @Date 2020/6/30
 * @Version 1.0
 **/
public final class UserCodeUtil {
    public static String getUserCode(String token, String channelId){
        Long userId = getUserIdByToken(token);
        String utoken = token + Consts.STR_SPLIT + userId + Consts.STR_SPLIT + channelId;
        return EncryptUtils.encrypt(utoken,EncryptUtils.DEF_PWD,EncryptUtils.SALT);
    }
    public static String getUserCode(String token, String channelId,UserDTO userDTO){
        String utoken = token + Consts.STR_SPLIT + userDTO.getUserId() + Consts.STR_SPLIT + channelId;

        return EncryptUtils.encrypt(utoken,EncryptUtils.DEF_PWD,EncryptUtils.SALT);
    }

    public static String getTokenByUserCode(String userCode){
        String utoken = EncryptUtils.decrypt(userCode,EncryptUtils.DEF_PWD,EncryptUtils.SALT);

        return Optional.ofNullable(utoken).flatMap(code -> {
            String result = null;
            if (code.indexOf(Consts.STR_SPLIT)>-1){
                result =  code.split(Consts.STR_SPLIT)[0];
            }
            return Optional.ofNullable(result);
        }).orElse(null);
    }

    public static String getUserToken(UserDTO userDTO){
        return Optional.ofNullable(userDTO).map(user -> {
            String token = user.getUserId() + Consts.STR_SPLIT + user.getUserName();
            return EncryptUtils.encrypt(token,EncryptUtils.DEF_PWD,EncryptUtils.SALT);
        }).orElse(null);
    }

    public static Long getUserIdByToken(String token){
        return Optional.ofNullable(token).flatMap(tk -> {
            String dtoken = EncryptUtils.decrypt(tk,EncryptUtils.DEF_PWD,EncryptUtils.SALT);
            Long userId = null;
            if (dtoken.indexOf(Consts.STR_SPLIT)>-1){
                userId = StringUtil.str2Long(dtoken.split(Consts.STR_SPLIT)[0]);
            }

            return Optional.ofNullable(userId);
        }).orElse(null);
    }
}
