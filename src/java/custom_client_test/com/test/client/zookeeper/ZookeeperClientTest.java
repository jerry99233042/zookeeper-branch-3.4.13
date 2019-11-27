package com.test.client.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

/**
 * Zookeeper自带的客户端
 * 示例：
 */
public class ZookeeperClientTest {

    public static void main(String[] args) {

        try{
            ZooKeeper client = new ZooKeeper("192.168.1.156:2181", 300,
                    event -> System.out.println(event));

            Stat stat = new Stat();

            try {
                client.getData("/zookeeper", new Watcher() {
                    @Override
                    public void process(WatchedEvent event) {
                        System.out.println("数据被改变");
                    }
                }, stat);
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            System.in.read();
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
