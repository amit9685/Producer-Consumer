package demo.message.controller;

import demo.message.service.ProducerConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProducerConsumerController {

    @Autowired
    ProducerConsumerService producerConsumerService;

    @GetMapping("/produce")
    public void login() throws InterruptedException {
        producerConsumerService.produceConsumer();
    }
}
