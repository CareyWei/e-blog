package com.dyzzw.blog.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dyzzw.blog.service.*;
import com.dyzzw.blog.shiro.AccountProfile;
import org.apache.catalina.security.SecurityUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

public class BaseController {
    @Autowired
    HttpServletRequest req;
    /*分页*/
    @Autowired
    IPostService postService;

    @Autowired
    ICommentService commentService;

    @Autowired
    IUserService userService;

    @Autowired
    IUserMessageService userMessageService;
    @Autowired
    IUserCollectionService userCollectionService;
    @Autowired
    ICategoryService categoryService;

    @Autowired
    WebService webService;

    @Autowired
    ISearchService searchService;

    @Autowired
    AmqpTemplate amqpTemplate;

    public Page  getPage(){
        int pn = ServletRequestUtils.getIntParameter(req, "pn",1);
        int size = ServletRequestUtils.getIntParameter(req, "size",2);
        Page page = new Page(pn, size);
        return page;
    }

    public AccountProfile getProfil(){
        return (AccountProfile) SecurityUtils.getSubject().getPrincipal();
    }
    protected Long getProfiledID(){
        return getProfil().getId();
    }
}
