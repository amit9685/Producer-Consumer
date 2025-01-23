package demo.message.service.impl;

import demo.message.consumer.Consumer;
import demo.message.producer.Producer;
import demo.message.dto.Message;
import demo.message.service.ProducerConsumerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ProducerConsumerServiceImpl implements ProducerConsumerService {

    private static final Logger log = LoggerFactory.getLogger(ProducerConsumerServiceImpl.class);

    @Override
    public void produceConsumer() throws InterruptedException {
        BlockingQueue<Message> queue = new LinkedBlockingQueue<>();
        AtomicInteger successfulCount = new AtomicInteger(0);
        AtomicInteger errorCount = new AtomicInteger(0);

        Producer producer = new Producer(queue);
        Consumer consumer = new Consumer(queue, successfulCount, errorCount);

        Thread producerThread = new Thread(producer);
        Thread consumerThread = new Thread(consumer);

        producerThread.start();
        consumerThread.start();

        producerThread.join();
        Thread.sleep(2000); // Allow consumer to finish
        consumerThread.interrupt(); // Stop consumer gracefully

        log.info("Total successful messages: {}", successfulCount.get());
        log.info("Total errors: {}", errorCount.get());
    }
}
