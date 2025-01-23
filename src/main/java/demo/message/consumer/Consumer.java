package demo.message.consumer;

import java.util.concurrent.BlockingQueue;

import demo.message.dto.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class Consumer implements Runnable {
    private final BlockingQueue<Message> queue;
    private final AtomicInteger successfulCount;
    private final AtomicInteger errorCount;

    private static final Logger log = LoggerFactory.getLogger(Consumer.class);

    public Consumer(BlockingQueue<Message> queue, AtomicInteger successfulCount, AtomicInteger errorCount) {
        this.queue = queue;
        this.successfulCount = successfulCount;
        this.errorCount = errorCount;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Message message = queue.take();
                log.info("Consumed: {}", message.getContent());
                successfulCount.incrementAndGet();
                Thread.sleep(900); // Simulate processing delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Consumer was interrupted.");
                errorCount.incrementAndGet();
                break;
            } catch (Exception e) {
                errorCount.incrementAndGet();
                log.error("Error processing message: {}", e.getMessage());
            }
        }
    }

    protected void processMessage(Message message) {
        log.info("Consumed: {}", message.getContent());
        successfulCount.incrementAndGet();
    }
}
