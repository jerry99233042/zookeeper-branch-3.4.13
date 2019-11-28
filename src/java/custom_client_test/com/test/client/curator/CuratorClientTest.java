package com.test.client.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * curator的zookeeper客户端测试
 */
public class CuratorClientTest {

    public static void main(String[] args) {

        // RetryNTimes参数为客户端连接服务端心跳重试策略，如果网络问题连接不上server，重试三次，每次间隔三秒
        CuratorFramework client = CuratorFrameworkFactory
                .newClient("192.168.1.156:2181", new RetryNTimes(3, 3000));

        // start才真正开启一个客户端
        client.start();

        try {

            // 创建节点，用临时节点mode，创建名为/data的节点，数据为1
            client.create().withMode(CreateMode.EPHEMERAL).forPath("/data", "1".getBytes());

            /*
             * 再上一步创建完节点后，我们要获取节点，并对该节点注册监听，curator的watch机制。
             * NodeCache 顾名思义，是一个节点的缓存对象，start之后才真正开启一个节点缓存
             * getListenable获取一个监听器容器集合，往里面添加一个监听器addListener
             */
            String path = "/data";

            NodeCache nodeCache = new NodeCache(client, path);// 针对哪个客户端的哪个节点来new一个节点缓存
            /*
             * 该start可以传参，默认为false
             * 意为在该NodeCache启动之时，是否要把对应节点的数据拿出来放到该缓存中
             * false: 启动时不把数据拿出放入缓存
             * true: 启动时把数据拿出来放入缓存
             * 注意：
             *      1、如果为false，则在添加监听器时就会触发一次nodeChanged函数，
             *      因为注册监听器时也会去节点拿出数据，拿出来和缓存对比，因为缓存为空，所以不一致，就会触发nodeChanged
             *      如果为true，则在添加监听器时不会触发nodeChanged
             *      2、触发nodeChanged函数的策略是只要修改了节点数据，即使修改的数据与前一次一致，都会触发nodeChanged
             */
            nodeCache.start();
            nodeCache.getListenable().addListener(new NodeCacheListener() {
                @Override
                public void nodeChanged() throws Exception {
                    System.out.println("数据改变");
                }
            });


            // 使用和zookeeper自带的客户端一样的watch机制
            client.getData().usingWatcher(new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    System.out.println("数据改变，和zookeeper自带的客户端一样的功能。");
                }
            }).forPath("/data");


            // 阻止线程中断
            System.in.read();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
