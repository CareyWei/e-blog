package com.dyzzw.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dyzzw.blog.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dyzzw.blog.vo.CommentVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jobob
 * @since 2020-07-02
 */
public interface ICommentService extends IService<Comment> {

    IPage<CommentVo> paging(Page page, Long postId, Long userId, String created);

    List<CommentVo> selectCountCo(QueryWrapper<Comment> commentQueryWrapper);
}
