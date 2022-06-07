package uz.example.flower.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uz.example.flower.config.QueueSender;

@RestController
@RequestMapping("api")
public class TestController {

    @Autowired
    private QueueSender queueSender;
    @GetMapping("rabbit")
    public String send() {
        queueSender.send("exchange_test", "router_key_test", "Text Message Flash Energy");
        return "Ok. Done";
    }
}
