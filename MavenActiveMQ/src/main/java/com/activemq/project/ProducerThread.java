package com.activemq.project;

public class ProducerThread implements Runnable {

    private Producer producer;

    public ProducerThread(Producer producer) {
        this.producer = producer;
    }

    @Override
    public void run() {
        try {
            System.out.println("send message start");
            producer.sendMessage("active-mq");
            System.out.println("send message end");
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
