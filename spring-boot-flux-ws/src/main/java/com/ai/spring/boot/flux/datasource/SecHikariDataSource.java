package com.ai.spring.boot.flux.datasource;

import com.ai.spring.im.common.util.EncryptUtils;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * Hikari DataSource
 *
 * @author 石头
 * @Date 2020/6/4
 * @Version 1.0
 **/
@Slf4j
@Setter
public class SecHikariDataSource extends HikariDataSource {
    /**是否加密*/
    private Boolean decrypt;
    /**加密的密码*/
    private String pwdkey;
    /**解密后的字符*/
    private String passwordDis;

    @Override
    public String getPassword(){
        // 如果已经解密了就直接返回
        if (!StringUtils.isEmpty(passwordDis)){
            return passwordDis;
        }
        String encPassword = super.getPassword();
        if (StringUtils.isEmpty(encPassword)){
            return null;
        }
        log.info("数据库密码加解密,=>{}",encPassword);
        if (decrypt){
            passwordDis =  EncryptUtils.decrypt(encPassword,pwdkey,EncryptUtils.SALT);
        }else {
            passwordDis = encPassword;
        }
        log.info("数据库密码加解密,<={}",passwordDis);
        return passwordDis;
    }
}
