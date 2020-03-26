package com.ai.spring.boot.test.zk;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * Curator ZK client
 *
 * @author 石头
 * @Date 2020/3/6
 * @Version 1.0
 **/
@Slf4j
public class CuratorZKclient {
    //Zk集群地址
    private static final String ZK_ADDRESS = "192.168.60.16:2181,192.168.60.17:2181,192.168.60.18:2181";
    private CuratorFramework client;
    public static CuratorZKclient instance = null;
    static {
        instance = new CuratorZKclient();
        instance.init();
    }
    private CuratorZKclient() {
    }
    public void init() {
        if (null != client) {
            return;
        }
        //创建客户端
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .connectString(ZK_ADDRESS)
                // 重试策略:第一次重试等待1s，第二次重试等待2s，第三次重试等待4s
                // 第一个参数：等待时间的基础单位，单位为毫秒
                // 第二个参数：最大重试次数
                .retryPolicy(new ExponentialBackoffRetry(1000,3))
                .connectionTimeoutMs(5000)
                .sessionTimeoutMs(5000);
        client = builder.build();

        //启动客户端实例,连接服务器
        client.start();
    }

    public void destroy() {
        CloseableUtils.closeQuietly(client);
    }
    /**
     * 创建节点
     */
    public void createNode(String zkPath, String data) {
        try {
            // 创建一个 ZNode 节点
            // 节点的数据为 payload
            byte[] payload = "to set content".getBytes("UTF-8");
            if (data != null) {
                payload = data.getBytes("UTF-8");
            }
            client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(zkPath, payload);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 删除节点
     */
    public void deleteNode(String zkPath) {
        try {
            if (!isNodeExist(zkPath)) {
                return;
            }
            client.delete()
                    .forPath(zkPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 检查节点
     */
    public boolean isNodeExist(String zkPath) {
        try {

            Stat stat = client.checkExists().forPath(zkPath);
            if (null == stat) {
                log.info("节点不存在:", zkPath);
                return false;
            } else {

                log.info("节点存在 stat is:", stat.toString());
                return true;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * 创建 临时 顺序 节点
     */
    public String createEphemeralSeqNode(String srcpath) {
        try {

            // 创建一个 ZNode 节点
            String path = client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                    .forPath(srcpath);

            return path;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public CuratorFramework getClient() {
        return client;
    }
}
