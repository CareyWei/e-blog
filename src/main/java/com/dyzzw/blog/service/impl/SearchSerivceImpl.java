package com.dyzzw.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dyzzw.blog.entity.Post;
import com.dyzzw.blog.search.model.PostDocment;
import com.dyzzw.blog.search.mq.PostMqIndexMessage;
import com.dyzzw.blog.search.repository.PostRepository;
import com.dyzzw.blog.service.IPostService;
import com.dyzzw.blog.service.ISearchService;
import com.dyzzw.blog.util.TypeChange;
import com.dyzzw.blog.vo.PostVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.util.QueryBuilder;
import org.dozer.DozerBeanMapper;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SearchSerivceImpl implements ISearchService {
    @Autowired
    PostRepository postRepository;

    @Autowired
   ModelMapper modelMapper;

    @Autowired
    IPostService postService;

    /**
     * es查询
     * @param page
     * @param keyword
     * @return
     */

    @Override
    public IPage search(Page page, String keyword) {

        //分页信息 mp的page转成jpa的
        Long current = page.getCurrent()-1;
        Long size = page.getSize();
        Pageable pageable = PageRequest.of(current.intValue(),size.intValue());

        //搜索es得到page的
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery
                (keyword, "title", "authodName", "categoryName");
        org.springframework.data.domain.Page<PostDocment> search = postRepository.search(multiMatchQueryBuilder, pageable);

        //结果信息jpa的pageDate转成mp的
        IPage pageDate = new Page(page.getCurrent(),page.getSize(),search.getTotalElements());
        pageDate.setRecords(search.getContent());
        return pageDate;
    }

    /**
     * 初始化ES查询
     * @param paging
     * @return
     */
    @Override
    public int initEsDate(List<PostVo> paging) {
        if(paging==null||paging.isEmpty()){
            return 0;
        }
        List<PostDocment> docments = new ArrayList<>();

        for(PostVo postVo:paging){
            //映射转换
            //modelMapper.addMappings(TypeChange.customField());
            PostDocment postDocment = modelMapper.map(postVo, PostDocment.class);
            docments.add(postDocment);
        }
        postRepository.saveAll(docments);
        return docments.size();
    }

    /**
     * 消息更新
     * @param message
     */
    @Override
    public void createOrUpdateIndex(PostMqIndexMessage message) {
        Long postId = message.getPostId();
        PostVo postVo =postService.selectOnewPost(new QueryWrapper<Post>().eq("p.id",postId));
        PostDocment postDocment = modelMapper.map(postVo, PostDocment.class);

        postRepository.save(postDocment);
        log.info("索引更新成功--->",postDocment.toString());
    }

    /**
     * 消息删除
     *
     * @param message
     */


    @Override
    public void removeIndex(PostMqIndexMessage message) {
        Long postId = message.getPostId();
        postRepository.deleteById(postId);
        log.info("索引删除成功--->",message.toString());
    }
}
