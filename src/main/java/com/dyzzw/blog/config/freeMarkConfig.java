package com.dyzzw.blog.config;


import com.dyzzw.blog.templates.PostsTemplate;
import com.dyzzw.blog.templates.RankTemplate;
import com.dyzzw.blog.templates.TemplateMethods;
import com.jagregory.shiro.freemarker.ShiroTags;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class freeMarkConfig {
    @Autowired
    private freemarker.template.Configuration configuration;
    @Autowired
    private PostsTemplate postsTemplate;
    @Autowired
    private RankTemplate rankTemplate;

    @PostConstruct
    public void setUp(){
        configuration.setSharedVariable("timeAgo",new TemplateMethods());
        configuration.setSharedVariable("posts",postsTemplate);
        configuration.setSharedVariable("hots",rankTemplate);
        configuration.setSharedVariable("shiro",new ShiroTags());

    }

/*
    @Bean
    public DozerBeanMapper mapper() {
        DozerBeanMapper mapper = new DozerBeanMapper();
        mapper.setMappingFiles(Arrays.asList("dozer/PostVoMapping.xml"));
        return mapper;
    }
*/

}
