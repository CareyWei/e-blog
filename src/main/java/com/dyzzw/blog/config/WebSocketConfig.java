package com.dyzzw.blog.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableAsync
@EnableWebSocketMessageBroker  //开启使用stomp协议传输基于代理的消息
public class WebSocketConfig  implements WebSocketMessageBrokerConfigurer {


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //user点对点推送，topic广播式推送 （发布订阅）
        registry.enableSimpleBroker("/user/","/topic/");//推送消息的前缀
        registry.setApplicationDestinationPrefixes("/app");//设置代理点
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //端点注册
        registry.addEndpoint("webSocket")//注册一个端点，访问地址
                .withSockJS();//表示当前浏览器不支持webSocket时可能会进行降级，采用其他技术
    }
}
