package com.dyzzw.blog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dyzzw.blog.common.lang.Result;
import com.dyzzw.blog.entity.Post;
import com.dyzzw.blog.vo.PostVo;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {


    /**
     * 加精、置顶、删除
     * @param id
     * @param rank 1表示操作  0表示取消
     * @param field
     * @return
     */
    @ResponseBody
    @PostMapping("/set")
        public Result jieSet(Long id ,Integer rank ,String field){
        Post post = postService.getById(id);
        Assert.notNull(post,"该帖子已被删除");
        if("delete".equals(field)){
          postService.removeById(id);
          return  Result.success();

        }else if("status".equals(field)){
            post.setRecommend(rank==1);
        }else if ("stick".equals(field)){
            post.setLevel(rank);
        }
        postService.updateById(post);
            return Result.success();
        }

    /**
     * es同步数据
     * @return
     */
    @ResponseBody
    @PostMapping("/initEsDate")
    public Result initEsDate(){
        //分批同步（数据量大）

        int size = 10000;
        Page page= new Page();
        page.setSize(size);

        long total=0;

        for(int i =1 ;i<1000;i++){
            page.setCurrent(i);

            IPage<PostVo> paging = postService.paging(page, null, null, null, null,null);

            /* 数据初始化(具体多少条) */

         int num=   searchService.initEsDate(paging.getRecords());
        total+=num;
         //当一页查不出10000条则是最后一页
            if(paging.getRecords().size()<size){
                break;
            }

        }

        return Result.success(null,"ES索引初始化成功，共"+total+"条记录");
    }

}
