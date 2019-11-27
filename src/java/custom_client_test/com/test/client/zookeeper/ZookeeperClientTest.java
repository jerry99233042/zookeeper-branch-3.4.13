package com.test.client.zookeeper;

import org.apache.zookeeper.KeeperException;
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

            // 用于保存节点数据之外的附加信息，比如，事务id，版本号，创建时间，修改时间之类的
            Stat stat = new Stat();

            try {
                // 获取zookeeper下的名为/zookeeper节点的数据，
                // 并注册一个监听器，监听该节点的变化，注意该监听器只能使用一次，在另一个客户端修改该节点数据时，会触发该监听器
                client.getData("/zookeeper", event -> System.out.println("数据被改变"), stat);
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            // 阻止线程中断
            System.in.read();

        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
