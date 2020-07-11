package com.dyzzw.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dyzzw.blog.search.mq.PostMqIndexMessage;
import com.dyzzw.blog.vo.PostVo;

import java.util.List;

public interface ISearchService  {
    IPage search(Page page, String q);

    int initEsDate(List<PostVo> paging);

    void createOrUpdateIndex(PostMqIndexMessage message);

    void removeIndex(PostMqIndexMessage message);
}
