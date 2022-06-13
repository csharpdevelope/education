//package uz.example.flower.config;
//
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.stereotype.Component;
//import uz.example.flower.utils.RabbitMqConstant;
//
//@Component
//public class QueueConsumer {
//
//    @RabbitListener(queues = {RabbitMqConstant.QUEUE_NAME})
//    public void receive(@Payload String fileBody) {
//        System.out.println("Message " + fileBody);
//    }
//}
