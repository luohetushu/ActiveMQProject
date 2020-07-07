package com.zookeeper.project.config;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Zookeeper 配置类
 */
@Configuration
public class ZookeeperConfig {

    private Logger logger = LoggerFactory.getLogger(ZookeeperConfig.class);

    @Value("zookeeper.address")
    private String connectAddress;

    @Value("zookeeper.timeout")
    private int timeout;

    //Zookeeper 客户端
    @Bean(name = "zkClient")
    public ZooKeeper zkClient(){
        ZooKeeper zooKeeper = null;

        try {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            //连接成功后，会回调watcher监听，此连接操作是异步的，执行完new语句后，直接调用后续代码
            zooKeeper = new ZooKeeper(connectAddress, timeout, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    //连接成功
                    if (Event.KeeperState.SyncConnected == watchedEvent.getState()){
                        countDownLatch.countDown();
                    }
                }
            });
            countDownLatch.await();
            logger.info("【初始化ZooKeeper连接状态....】= {}", zooKeeper.getState());
        } catch (IOException | InterruptedException e) {
            //e.printStackTrace();
            logger.error("初始化ZooKeeper连接异常....】= {}", e.getMessage());
        }
        return zooKeeper;
    }

}
