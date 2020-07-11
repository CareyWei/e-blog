package com.dyzzw.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dyzzw.blog.entity.Category;
import com.dyzzw.blog.entity.Post;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dyzzw.blog.vo.PostVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jobob
 * @since 2020-07-02
 */
public interface IPostService extends IService<Post> {

    IPage paging(Page page, Integer category, Integer level, Boolean recommend,Long userId,String created);

    PostVo selectOnewPost(QueryWrapper<Post> wapper);

    /**
     * redis缓存本周热议
     */
    void initWeekRank();

    /*评论自增*/
    void incrCommentCountAndUnionForWeekRank(long postId,boolean isIncr);

    void setViewCount(PostVo vo);
}
