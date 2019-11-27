package com.test.client.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;

/**
 * curator的zookeeper客户端测试
 */
public class CuratorClientTest {

    public static void main(String[] args) {

        //RetryNTimes参数为客户端连接服务端心跳重试策略，重试三次，每次间隔一秒
        CuratorFramework client = CuratorFrameworkFactory
                .newClient("192.168.1.156:2181", new RetryNTimes(3, 1000));

        //start才真正开启一个客户端
        client.start();

        try {
            //创建节点，用临时节点mode，创建名为/data的节点，数据为1
//            client.create().withMode(CreateMode.EPHEMERAL).forPath("/data", "1".getBytes());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
