//package uz.example.flower.config;
//
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.core.MessageProperties;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.stereotype.Component;
//
//@Component
//public class QueueSender {
//
//    private final RabbitTemplate rabbitTemplate;
//
//    public QueueSender(RabbitTemplate rabbitTemplate) {
//        this.rabbitTemplate = rabbitTemplate;
//    }
//
//    public void send(String exchange, String key, String order) {
//        MessageProperties messageProperties = new MessageProperties();
//        messageProperties.setHeader("ultima", "sim");
//        Message message = new Message(order.getBytes(), messageProperties);
//        rabbitTemplate.convertAndSend(exchange, key, order);
//    }
//}
