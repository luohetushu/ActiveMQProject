package com.activemq.project;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 生产者/发布者：创建并发送消息的客户端
 */
public class Producer {

    //ActiveMQ 的默认用户名
    private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;
    //ActiveMQ 的默认登录密码
    private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
    //ActiveMQ 的链接地址
    private static final String BROKEN_URL = ActiveMQConnection.DEFAULT_BROKER_URL;

    //创建会话, 通过session创建生产者、消费者、消息等
    private static Session session;

    private ThreadLocal<MessageProducer> threadLocal = new ThreadLocal<>();

    AtomicInteger count = new AtomicInteger();

    static {
        try {
            //创建一个链接工厂
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(USERNAME, PASSWORD, BROKEN_URL);
            //从工厂中创建一个链接对象
            Connection connection = connectionFactory.createConnection();
            //开启连接
            connection.start();
            // 创建会话，设置会话事务级别
            //当我们需要使用session发送/接收多个消息时，可以将这些发送/接收动作放到一个事务中
            session = connection.createSession(true, Session.SESSION_TRANSACTED);
            System.out.println("session built");
        } catch (JMSException e){
            e.printStackTrace();
        }
    }

    public void sendMessage(String destName){
        try {
            //消息生产者
            MessageProducer messageProducer = null;

            if (threadLocal.get() != null){
                messageProducer = threadLocal.get();
            } else {
                //创建一个消息队列
                Queue queue = session.createQueue(destName);
                messageProducer = session.createProducer(queue);
                threadLocal.set(messageProducer);
            }

            int num = count.getAndIncrement(); // 返回的是当前值
            String message = Thread.currentThread().getName() +
                    "，producer:我是大帅哥，我现在正在生产东西！count:" + num;
            //创建一条消息
            TextMessage msg = session.createTextMessage(message);
            System.out.println(message);
            //发送消息
            messageProducer.send(msg);
            //提交事务
            session.commit();

        } catch (JMSException  e){
            e.printStackTrace();
        }
    }


}
