package com.dyzzw.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dyzzw.blog.entity.Comment;
import com.dyzzw.blog.vo.CommentVo;
import org.elasticsearch.search.SearchService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController  extends BaseController{

    @RequestMapping({"","/","index"})
    public String index(String Tid) throws NoSuchFieldException {

            Boolean recommend=null;

            if("1".equals(Tid)){
                recommend =true;
            }

            //1分页信息、2分类、3置顶、4精选、5用户、6排序
            IPage results = postService.paging(getPage(), null, null,recommend, null, "created");

        IPage results2 = postService.paging(getPage(), null, 1,recommend, null, "created");

        //统计回复数
            List<CommentVo>  commentVo= commentService.selectCountCo(new QueryWrapper<Comment>());

            req.setAttribute("currentCategoryID", 0);
            req.setAttribute("pageDate", results);
        req.setAttribute("pageDate2", results2);
            req.setAttribute("Counts",commentVo);


        return "index";
    }

    @RequestMapping("/search")
    public String search(String q){

        IPage pageDate = searchService.search(getPage(),q);

        req.setAttribute("q",q);
        req.setAttribute("currentCategoryID", 1111111111);
        req.setAttribute("pageData", pageDate);
        return "search";
    }
}
