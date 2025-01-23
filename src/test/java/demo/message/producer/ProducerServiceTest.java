package demo.message.producer;

import demo.message.dto.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProducerServiceTest {

    private BlockingQueue<Message> queue;
    private Producer producer;

    @BeforeEach
    public void setUp() {
        queue = new LinkedBlockingQueue<>();
        producer = new Producer(queue);
    }

    @Test
    public void testRun() throws InterruptedException {
        Thread producerThread = new Thread(producer);
        producerThread.start();
        producerThread.join();

        assertEquals(10, queue.size(), "Queue should contain 10 messages.");
        for (int i = 1; i <= 10; i++) {
            assertEquals("Message " + i, queue.take().getContent());
        }
    }
}
