package com.ai.spring.boot.test.zk;

import com.ai.spring.boot.test.thread.FutureTaskScheduler;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.junit.Test;

/**
 * TODO
 *
 * @author 石头
 * @Date 2020/3/6
 * @Version 1.0
 **/
@Slf4j
public class ZkLockTester {
    int count = 0;
    @Test
    public void testLock() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            FutureTaskScheduler.add(() -> {
                ZkLock lock = new ZkLock();
                lock.lock();

                for (int j = 0; j < 10; j++) {

                    count++;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("count = " + count);
                lock.unlock();

            });
        }

        Thread.sleep(Integer.MAX_VALUE);
    }

    @Test
    public void testzkMutex() throws InterruptedException {

        CuratorFramework client = CuratorZKclient.instance.getClient();
        final InterProcessMutex zkMutex = new InterProcessMutex(client, "/mutex");

        for (int i = 0; i < 10; i++) {
            FutureTaskScheduler.add(() -> {

                try {
                    // /获取互斥锁
                    zkMutex.acquire();

                    for (int j = 0; j < 10; j++) {
                        // 公共的资源变量累加
                        count++;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    log.info("count = " + count);
                    // 释放互斥锁
                    zkMutex.release();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
        }

        Thread.sleep(Integer.MAX_VALUE);
    }
}
