package com.ai.spring.boot.user.dao;

import com.ai.spring.boot.user.dao.bean.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户数据操作类
 *
 * @author 石头
 * @Date 2020/3/25
 * @Version 1.0
 **/
@Repository
public class UserDao {
    private static final Map<Long ,User> cachedUsers = new HashMap<>(8);
    static {
        cachedUsers.put(1L,User.builder().userId(1L).age(20).userName("张三").wxId("wx000001").roles("user"));
        cachedUsers.put(2L,User.builder().userId(2L).age(25).userName("李四").wxId("wx000002").roles("user"));
        cachedUsers.put(3L,User.builder().userId(3L).age(22).userName("王五").wxId("wx000003").roles("admin"));
        cachedUsers.put(4L,User.builder().userId(4L).age(28).userName("赵六").wxId("wx000004").roles("user"));
    }

    public User selectByPrimaryKey(Long userId){
        return cachedUsers.get(userId);
    }
}
