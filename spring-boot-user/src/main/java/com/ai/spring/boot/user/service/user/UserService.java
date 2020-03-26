package com.ai.spring.boot.user.service.user;

import com.ai.spring.boot.user.convert.UserDTOConvert;
import com.ai.spring.boot.user.dao.UserDao;
import com.ai.spring.boot.user.dao.bean.User;
import com.ai.spring.boot.user.service.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户服务
 *
 * @author 石头
 * @Date 2020/3/25
 * @Version 1.0
 **/
@Service
@Slf4j
public class UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserDTOConvert convert;

    public UserDTO findById(Long userId){
        log.info("我请调用了----");
        User user = userDao.selectByPrimaryKey(userId);
        return convert.toDto(user);
    }
}
