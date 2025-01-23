package demo.message.service;

import demo.message.dto.Message;
import demo.message.service.impl.ProducerConsumerServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ProducerConsumerServiceImplTest {

    @Autowired
    private ProducerConsumerServiceImpl service;

    @Test
    public void testProduceConsumer() throws InterruptedException {
        assertNotNull(service, "Service should not be null.");
        service.produceConsumer();

    }
}
