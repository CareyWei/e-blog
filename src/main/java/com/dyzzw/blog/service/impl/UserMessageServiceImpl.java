package com.dyzzw.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dyzzw.blog.entity.UserMessage;
import com.dyzzw.blog.mapper.UserMessageMapper;
import com.dyzzw.blog.service.IUserMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-07-02
 */
@Service
public class UserMessageServiceImpl extends ServiceImpl<UserMessageMapper, UserMessage> implements IUserMessageService {

    @Autowired
    UserMessageMapper userMessageMapper;
    @Override
    public IPage paging(Page page, QueryWrapper<UserMessage> wrapper) {


        return userMessageMapper.selectMessage(page,wrapper);
    }

    @Override
    public void updateToRead(List<Long> ids) {
        if(ids.isEmpty())return;
        userMessageMapper.updateToRead(new QueryWrapper<UserMessage>()
        .in("id",ids));
    }
}
