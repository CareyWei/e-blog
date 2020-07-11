package com.dyzzw.blog.search.repository;

import com.dyzzw.blog.search.model.PostDocment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * 文档仓库
 */
@Repository
public interface PostRepository  extends ElasticsearchRepository<PostDocment,Long> {
    //符合JPA的命名规范的接口


}
