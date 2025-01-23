package demo.message.consumer;

import demo.message.dto.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConsumerTest {

    private BlockingQueue<Message> queue;
    private AtomicInteger successfulCount;
    private AtomicInteger errorCount;
    private Consumer consumer;

    @BeforeEach
    public void setUp() {
        queue = new LinkedBlockingQueue<>();
        successfulCount = new AtomicInteger(0);
        errorCount = new AtomicInteger(0);
        consumer = new Consumer(queue, successfulCount, errorCount);
    }

    @Test
    public void testRunSuccessfullyProcessesMessages() throws InterruptedException {
        queue.put(new Message("Message 1"));
        queue.put(new Message("Message 2"));

        CountDownLatch latch = new CountDownLatch(2);
        Consumer wrappedConsumer = new Consumer(queue, successfulCount, errorCount) {
            @Override
            public void processMessage(Message message) {
                super.processMessage(message);
                latch.countDown(); // Decrement latch after each message is processed
            }
        };

        Thread consumerThread = new Thread(wrappedConsumer);
        consumerThread.start();

        // Wait for all messages to be processed
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Messages were not processed in time.");
        consumerThread.interrupt(); // Interrupt the thread after processing
        consumerThread.join();

        // Assert
        assertEquals(2, successfulCount.get(), "Two messages should have been processed successfully.");
        assertEquals(0, errorCount.get(), "There should be no errors.");
    }

    @Test
    public void testRunHandlesInterruptionGracefully() throws InterruptedException {
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();

        Thread.sleep(1000); // Start processing
        consumerThread.interrupt(); // Interrupt the thread
        consumerThread.join();

        assertTrue(errorCount.get() > 0, "Errors should be counted if interrupted.");
    }
}
