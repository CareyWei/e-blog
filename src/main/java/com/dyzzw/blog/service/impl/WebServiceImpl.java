package com.dyzzw.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dyzzw.blog.entity.UserMessage;
import com.dyzzw.blog.service.IUserMessageService;
import com.dyzzw.blog.service.WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class WebServiceImpl implements WebService {
    @Autowired
    IUserMessageService messageService;
    //系统通知操作模板
    @Autowired
    SimpMessagingTemplate messagingTemplate;
    //异步通信
    @Async
    @Override
    public void sendMsgCountToUser(Long toUserId) {
        //未读消息
        int count=  messageService.count(new QueryWrapper<UserMessage>()
                .eq("to_user_id",toUserId)
                .eq("status","0"));

        //发送通知websocket通道/user/7/messCount/
        messagingTemplate.convertAndSendToUser(toUserId.toString(),"/messCount/",count);
    }
}
