package com.activemq.project;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 消费者/订阅者：接收并处理消息的客户端
 */
public class Consumer {

    //ActiveMQ 的默认用户名
    private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;
    //ActiveMQ 的默认登录密码
    private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
    //ActiveMQ 的链接地址
    private static final String BROKEN_URL = ActiveMQConnection.DEFAULT_BROKER_URL;

    //创建会话, 通过session创建生产者、消费者、消息等
    private static Session session;

    private ThreadLocal<MessageConsumer> threadLocal = new ThreadLocal<>();

    AtomicInteger count = new AtomicInteger();

    static {
        try {
            //创建一个链接工厂
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(USERNAME, PASSWORD, BROKEN_URL);
            //从工厂中创建一个链接对象
            Connection connection = connectionFactory.createConnection();
            //开启连接
            connection.start();
            // 创建会话，自动接收
            session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        } catch (JMSException e){
            e.printStackTrace();
        }
    }

    public void getMessage(String destName){
        try {
            //创建一个消息队列
            Queue queue = session.createQueue(destName);
            //消息消费者
            MessageConsumer messageConsumer = null;

            if (threadLocal.get() != null){
                messageConsumer = threadLocal.get();
            } else {
                messageConsumer = session.createConsumer(queue);
                threadLocal.set(messageConsumer);
            }
            TextMessage msg = (TextMessage) messageConsumer.receive();
            if (msg != null) {
                msg.acknowledge();
                System.out.println(Thread.currentThread().getName() + " : Consumer:我是消费者，我正在消费Msg："
                        + msg.getText() + "--->count: " + count.getAndIncrement());
            }

        } catch (JMSException e){
            e.printStackTrace();
        }

    }

}
