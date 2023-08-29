package tech.baisi.mc.echo.BackEnd.Configs;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    private static String EXCHANGE_NAME = "mc_exchange";
    private static String QUEUE_NAME = "mc_queue";
    private static String ROUTING = "mc_shop_routing";

    @Bean
    public DirectExchange getExchange(){
        return new DirectExchange(EXCHANGE_NAME,true,false);
    }

    @Bean
    public Queue getQueue(){
        return new Queue(QUEUE_NAME,true,false,false);
    }

    @Bean
    public Binding getBinding(){
        return BindingBuilder.bind(getQueue()).to(getExchange()).with(ROUTING);
    }
}
