package com.dyzzw.blog.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dyzzw.blog.entity.Category;
import com.dyzzw.blog.service.ICategoryService;
import com.dyzzw.blog.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.util.List;

/**
 * 运行时利用会话信息查找
 * 因此不用一直查库
 */
@Component
public class ContextStartup implements ApplicationRunner , ServletContextAware {

    @Autowired
    ICategoryService categoryService;
    @Autowired
    IPostService postService;
    ServletContext servletContext;
    @Override
    public void run(ApplicationArguments args) throws Exception {
         List<Category> list =  categoryService.list(new QueryWrapper<Category>()
            .eq("status",0));
         servletContext.setAttribute("list",list);
    /*本周热议*/
         postService.initWeekRank();
}

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext =servletContext;
    }
}
