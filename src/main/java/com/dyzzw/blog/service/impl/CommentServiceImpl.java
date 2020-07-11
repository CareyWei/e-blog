package com.dyzzw.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dyzzw.blog.entity.Comment;
import com.dyzzw.blog.mapper.CommentMapper;
import com.dyzzw.blog.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dyzzw.blog.vo.CommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Autowired
    CommentMapper commentMapper;
    @Override
    public IPage<CommentVo> paging(Page page, Long postId, Long userId, String created) {
        return commentMapper.selectComment(page,new QueryWrapper<Comment>()
                .eq(postId!=null,"post_id",postId)
                .eq(userId!=null,"user_id",userId)
                .orderByDesc(created!=null,created));
    }

    @Override
    public List<CommentVo> selectCountCo(QueryWrapper<Comment> commentQueryWrapper) {


        return commentMapper.selectCountCo(commentQueryWrapper);
    }
}
