package com.ai.spring.boot.test.zk;

/**
 * 分布式锁接口
 *
 * @author 石头
 * @Date 2020/3/6
 * @Version 1.0
 **/
public interface Lock {
    boolean lock() throws Exception;

    boolean unlock();
}
