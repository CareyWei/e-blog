package com.dyzzw.blog.templates;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dyzzw.blog.common.templates.DirectiveHandler;
import com.dyzzw.blog.common.templates.TemplateDirective;
import com.dyzzw.blog.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostsTemplate extends TemplateDirective {
    @Autowired
    IPostService postService;

    @Override
    public String getName() {
        return "posts";
    }

    @Override
    public void execute(DirectiveHandler handler) throws Exception {
        Integer level = handler.getInteger("level");
        Integer pn = handler.getInteger("pn",2);
        Integer size=handler.getInteger("size",2);
        Integer categoryId =  handler.getInteger("categoryId");
        System.out.println(level);
        IPage page =postService.paging(new Page(pn,size), categoryId,level,null,null,"created");
        handler.put(RESULTS,page).render();
    }
}
