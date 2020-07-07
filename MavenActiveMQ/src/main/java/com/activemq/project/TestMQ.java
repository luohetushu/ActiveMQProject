package com.activemq.project;

import org.junit.Test;

public class TestMQ {

    @Test
    public void testProducer(){
        Producer producer = new Producer();
        new Thread(new ProducerThread(producer), "生产者线程1").start();
        //new Thread(new ProducerThread(producer), "生产者线程2").start();
        //new Thread(new ProducerThread(producer), "生产者线程3").start();
    }

    @Test
    public void testConsumer(){
        Consumer consumer = new Consumer();
        new Thread(new ConsumerThread(consumer), "消费者者线程1").start();
        //new Thread(new ConsumerThread(consumer), "消费者者线程2").start();
        //new Thread(new ConsumerThread(consumer), "消费者者线程3").start();
    }

}
