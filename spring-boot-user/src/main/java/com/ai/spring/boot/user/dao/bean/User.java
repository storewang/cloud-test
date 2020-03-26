package com.ai.spring.boot.user.dao.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户数据类
 *
 * @author 石头
 * @Date 2020/3/25
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
public class User {
    private Long userId;
    private String userName;
    private Integer age;
    private String wxId;
    private String roles;


    public static User builder(){
        return new User();
    }

    public User userId(Long userId){
        setUserId(userId);
        return this;
    }
    public User age(Integer age){
        setAge(age);
        return this;
    }
    public User userName(String userName){
        setUserName(userName);
        return this;
    }

    public User wxId(String wxId){
        setWxId(wxId);
        return this;
    }
    public User roles(String roles){
        setRoles(roles);
        return this;
    }
}
