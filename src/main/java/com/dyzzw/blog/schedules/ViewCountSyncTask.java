package com.dyzzw.blog.schedules;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dyzzw.blog.entity.Post;
import com.dyzzw.blog.service.IPostService;
import com.dyzzw.blog.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class ViewCountSyncTask {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    IPostService postService;

    @Scheduled(fixedRate = 10000)//每分钟同步
    public void task(){
           Set<String> set = redisTemplate.keys("rank:post*");
        List<String> ids = new ArrayList<>();
        for (String key:set) {
            if(redisUtil.hHasKey(key,"post:viewCount")){
                ids.add(key.substring("rank:post".length()));
            }
        }
        if(ids.isEmpty())return;

          //更新阅读量
        List<Post> posts = postService.list(new QueryWrapper<Post>().in("id", ids));
        posts.stream().forEach((post)->{
            Integer viewCount = (Integer) redisUtil.hget("rank:post" + post.getId(), "post:viewCount");
            post.setViewCount(viewCount);
        });

        if(posts.isEmpty())return;

        boolean isSucc = postService.updateBatchById(posts);
        if(isSucc){
            ids.stream().forEach((id)->{
                redisUtil.hdel("rank:post" + id, "post:viewCount");
                System.out.println(id+"-----------------同步成功");
            });

        }
    }
}
