package uz.example.flower.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TelegramNotifier {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Value("${telegram.token}")
    private String token;
    @Value("${telegram.chatId}")
    private String chatId;
}
