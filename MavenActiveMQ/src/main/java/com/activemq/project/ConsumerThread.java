package com.activemq.project;

public class ConsumerThread implements Runnable {

    private Consumer consumer;

    public ConsumerThread(Consumer consumer) {
        this.consumer = consumer;
    }

    @Override
    public void run() {
        try {
            System.out.println("get message start");
            consumer.getMessage("active-mq");
            System.out.println("get message end");
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
