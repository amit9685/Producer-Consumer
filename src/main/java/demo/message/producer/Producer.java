package demo.message.producer;

import demo.message.dto.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;


public class Producer implements Runnable {
    private final BlockingQueue<Message> queue;

    public Producer(BlockingQueue<Message> queue) {
        this.queue = queue;
    }

    private static final Logger log = LoggerFactory.getLogger(Producer.class);

    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            try {
                Message message = new Message("Message " + i);
                queue.put(message);
                log.info("Produced: {}", message.getContent());
                Thread.sleep(500); // Simulate delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Producer was interrupted.");
            }
        }
    }
}
