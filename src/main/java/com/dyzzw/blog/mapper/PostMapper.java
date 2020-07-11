package com.dyzzw.blog.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dyzzw.blog.entity.Post;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dyzzw.blog.vo.PostVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2020-07-02
 */
@Component
public interface PostMapper extends BaseMapper<Post> {

    IPage<PostVo> selectPosts(Page page, @Param(Constants.WRAPPER)QueryWrapper<Post> wapper);

    PostVo selectOnePost(@Param(Constants.WRAPPER)QueryWrapper<Post> wapper);
}
