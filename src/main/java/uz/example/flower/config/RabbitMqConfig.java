//package uz.example.flower.config;
//
//import org.springframework.amqp.core.*;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import uz.example.flower.utils.RabbitMqConstant;
//
//@Configuration
//public class RabbitMqConfig {
//
//    @Bean
//    public Queue queue() {
//        return new Queue(RabbitMqConstant.QUEUE_NAME, true);
//    }
//
//    @Bean
//    public DirectExchange exchange(){
//        return new DirectExchange(RabbitMqConstant.EXCHANGE_NAME);
//    }
//
//    @Bean
//    public Binding binding(Queue queue, DirectExchange exchange) {
//        return BindingBuilder.bind(queue)
//                .to(exchange)
//                .with(RabbitMqConstant.ROUTING_KEYS);
//    }
//}
