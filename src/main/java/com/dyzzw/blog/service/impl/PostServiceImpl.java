package com.dyzzw.blog.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dyzzw.blog.entity.Post;
import com.dyzzw.blog.mapper.PostMapper;
import com.dyzzw.blog.service.IPostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.dyzzw.blog.util.RedisUtil;
import com.dyzzw.blog.vo.PostVo;;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements IPostService {
    @Autowired
    PostMapper postMapper;

    @Autowired
    RedisUtil redisUtil;
    /*博客分页*/
    @Override
    public IPage paging(Page page, Integer category, Integer level, Boolean recommend, Long userId, String created) {
       if(level==null){
           level=-1;
       }
        QueryWrapper<Post> wapper = (QueryWrapper<Post>) new QueryWrapper()
                .eq(category!=null,"category_id",category)
                .eq(userId!=null,"user_id",userId)
                .eq(level==0,"level",0)
                .gt(level>0,"level",0)
                .eq(recommend!=null,"recommend",recommend)
                .orderByAsc(created);

        return postMapper.selectPosts(page,wapper);
    }
    /*博客内容*/
    @Override
    public PostVo selectOnewPost(QueryWrapper<Post> wapper) {
        return postMapper.selectOnePost(wapper);
    }
    /*本周热议初始话*/
    @Override
    public void initWeekRank() {
        //获取7天的发表文章
        List<Post> list = this.list(new QueryWrapper<Post>()
        .ge("created", DateUtil.offsetDay(new Date(),-7))
        .select("id","title","user_id","comment_count","view_count","created")
        );
        //初始化总评论量

        for (Post p:list) {
            String key ="day:rank:"+DateUtil.format(p.getCreated(), DatePattern.PURE_DATE_FORMAT);
            redisUtil.zSet(key,p.getId(),p.getCommentCount());
            //7天后自动过期
          long time =DateUtil.between(new Date(),p.getCreated(), DateUnit.DAY);
            long expirtime = (7-time)*24*60*60;
            redisUtil.expire(key,expirtime);
            //缓存文章的基本信息（id、标题、评论数量、作者）
            this.hasnCachePostIdAndTitle(p,expirtime);
        }


        //做并集
        this.zunionAndStoreLast7DayForWeekRank();
    }

    /**
     * 评论自增
     * @param postId
     * @param isIncr
     */
    @Override
    public void incrCommentCountAndUnionForWeekRank(long postId, boolean isIncr) {
        String currentDay ="day:rank:"+DateUtil.format(new Date(), DatePattern.PURE_DATE_FORMAT);
        redisUtil.zIncrementScore(currentDay,postId,isIncr?1:-1);
        Post p = this.getById(postId);
        //7天后自动过期
        long time =DateUtil.between(new Date(),p.getCreated(), DateUnit.DAY);
        //过期时间
        long expirtime = (7-time)*24*60*60;
        //缓存新文章的基本信息
        hasnCachePostIdAndTitle( p, expirtime);
        //重新做并集
        this.zunionAndStoreLast7DayForWeekRank();
    }

    @Override
    public void setViewCount(PostVo vo) {
        String key ="rank:post"+vo.getId();
        //1、从缓存中获取viewcount
     Integer viewCount= (Integer) redisUtil.hget(key,"post:viewCount");
        //2、如果没有，就先从实体里面获取，再加一

        if(viewCount!=null){
            vo.setViewCount((viewCount+1));
        }else{
            vo.setViewCount(vo.getViewCount()+1);
        }

        //3、同步到缓存里面
        redisUtil.hset(key,"post:viewCount",vo.getViewCount());
    }

    /**
     * 本周每日评论的总和
      */
    private void zunionAndStoreLast7DayForWeekRank() {
        String currentDay ="day:rank:"+DateUtil.format(new Date(), DatePattern.PURE_DATE_FORMAT);

        String destkey ="week:rank";
        List<String> orderKeys = new ArrayList<>();
        for (int i = -6; i <0 ; i++) {
            String temp ="day:rank:"+
                    DateUtil.format(DateUtil.offsetDay(new Date(),i) ,DatePattern.PURE_DATE_FORMAT);
            orderKeys.add(temp);
        }
       redisUtil.zUnionAndStore(currentDay,orderKeys,destkey);
    }

    /**
     * 缓存文章的基本信息
     * @param p
     * @param expirtime
     */
    private void hasnCachePostIdAndTitle(Post p,long expirtime) {
        String key = "rank:post"+p.getId();
       boolean haskey= redisUtil.hasKey(key);
       if(!haskey){
           redisUtil.hset(key,"post:id",p.getId(),expirtime);
           redisUtil.hset(key,"post:title",p.getTitle(),expirtime);
           redisUtil.hset(key,"post:commentCount",p.getCommentCount(),expirtime);
           redisUtil.hset(key,"post:viewCount",p.getViewCount(),expirtime);

       }
    }
}
