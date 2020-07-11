package com.dyzzw.blog.search.mq;

import com.dyzzw.blog.config.RabbitConfig;
import com.dyzzw.blog.service.ISearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RabbitListener(queues = RabbitConfig.ES_QUEUE)
public class MqMessageHandler {

    @Autowired
    ISearchService searchService;

    @RabbitHandler
    public void handler(PostMqIndexMessage message){

        log.info("mq收到一条消息：",message.toString());

           switch (message.getType()){
               case PostMqIndexMessage.CREATE_OR_UPDATE:
                   searchService.createOrUpdateIndex(message);
                   break;
               case  PostMqIndexMessage.REMOVE:
                   searchService.removeIndex(message);
                   break;
               default:
                   log.error("没有找到消息的类型，请注意！！-》",message.toString());
                    break;

           }
        }

}
