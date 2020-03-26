package com.ai.spring.boot.test.zk;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 分布式锁zk实现
 *
 * @author 石头
 * @Date 2020/3/6
 * @Version 1.0
 **/
@Slf4j
public class ZkLock implements Lock{
    //ZkLock的节点链接
    private static final String ZK_PATH = "/test/lock";
    private static final String LOCK_PREFIX = ZK_PATH + "/";
    private static final long WAIT_TIME = 1000;
    //Zk客户端
    CuratorFramework client = null;

    private String locked_short_path = null;
    private String locked_path = null;
    private String prior_path = null;
    final AtomicInteger lockCount = new AtomicInteger(0);
    private Thread thread;

    public ZkLock() {
        if (!CuratorZKclient.instance.isNodeExist(ZK_PATH)) {
            CuratorZKclient.instance.createNode(ZK_PATH, null);
        }
        client = CuratorZKclient.instance.getClient();
    }
    @Override
    public boolean lock() throws Exception {
        // 可重入，确保同一线程可以重复加锁
        synchronized (this) {
            if (lockCount.get() == 0) {
                thread = Thread.currentThread();
                lockCount.incrementAndGet();
            } else {
                if (!thread.equals(Thread.currentThread())) {
                    return false;
                }
                lockCount.incrementAndGet();
                return true;
            }
        }
        try{
            boolean locked = false;

            locked = tryLock();
            if (locked) {
                return true;
            }
            while (!locked) {
                await();
                // 获取等待的子节点列表
                List<String> waiters = getWaiters();
                if (checkLocked(waiters)) {
                    locked = true;
                }
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            unlock();
        }
        return false;
    }

    @Override
    public boolean unlock() {
        if (!thread.equals(Thread.currentThread())) {
            return false;
        }

        int newLockCount = lockCount.decrementAndGet();
        if (newLockCount < 0) {
            throw new IllegalMonitorStateException("Lock count has gone negative for lock: " + locked_path);
        }
        if (newLockCount != 0) {
            return true;
        }
        try {
            if (CuratorZKclient.instance.isNodeExist(locked_path)) {
                client.delete().forPath(locked_path);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void await() throws Exception {
        if (null == prior_path) {
            throw new Exception("prior_path error");
        }
        final CountDownLatch latch = new CountDownLatch(1);
        //订阅比自己次小顺序节点的删除事件
        Watcher w = new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("监听到的变化 watchedEvent = " + watchedEvent);
                log.info("[WatchedEvent]节点删除");

                latch.countDown();
            }
        };
        client.getData().usingWatcher(w).forPath(prior_path);

        latch.await(WAIT_TIME, TimeUnit.SECONDS);
    }
    private boolean tryLock() throws Exception {
        //创建临时Znode
        List<String> waiters = getWaiters();
        locked_path = CuratorZKclient.instance.createEphemeralSeqNode(LOCK_PREFIX);
        if (null == locked_path) {
            throw new Exception("zk error");
        }
        locked_short_path = getShorPath(locked_path);
        //获取等待的子节点列表，判断自己是否第一个
        if (checkLocked(waiters)) {
            return true;
        }
        // 判断自己排第几个
        int index = Collections.binarySearch(waiters, locked_short_path);
        if (index < 0) { // 网络抖动，获取到的子节点列表里可能已经没有自己了
            throw new Exception("节点没有找到: " + locked_short_path);
        }
        //如果自己没有获得锁，则要监听前一个节点
        prior_path = ZK_PATH + "/" + waiters.get(index - 1);

        return false;
    }
    private String getShorPath(String locked_path) {

        int index = locked_path.lastIndexOf(ZK_PATH + "/");
        if (index >= 0) {
            index += ZK_PATH.length() + 1;
            return index <= locked_path.length() ? locked_path.substring(index) : "";
        }
        return null;
    }

    private boolean checkLocked(List<String> waiters) {
        //节点按照编号，升序排列
        Collections.sort(waiters);
        // 如果是第一个，代表自己已经获得了锁
        if (locked_short_path.equals(waiters.get(0))) {
            log.info("成功的获取分布式锁,节点为{}", locked_short_path);
            return true;
        }
        return false;
    }
    protected List<String> getWaiters() {
        List<String> children = null;
        try {
            children = client.getChildren().forPath(ZK_PATH);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return children;
    }
}
