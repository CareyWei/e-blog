package com.dyzzw.blog.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class RabbitConfig  {
    public final  static String ES_QUEUE="es_queue";
    public final  static String ES_CHANGE="es_change";
    public final  static String ES_BINDING_KEY="es_change";

    //队列
    @Bean
    public Queue exQueue(){
        return  new Queue(ES_QUEUE);
    }

    //交换机

    @Bean
    DirectExchange exchange(){
        return new DirectExchange(ES_CHANGE);
    }

    //绑定
    @Bean
    Binding binding(Queue queue ,DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ES_BINDING_KEY);
    }
}
