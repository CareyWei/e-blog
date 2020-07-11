package com.dyzzw.blog.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dyzzw.blog.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dyzzw.blog.vo.CommentVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2020-07-02
 */
@Component
public interface CommentMapper extends BaseMapper<Comment> {

    IPage<CommentVo> selectComment(Page page, @Param(Constants.WRAPPER)QueryWrapper<Comment> orderByAsc);

    List<CommentVo> selectCountCo(@Param(Constants.WRAPPER)QueryWrapper<Comment> orderByAsc);
}
