package com.dyzzw.blog.service.impl;

import com.dyzzw.blog.entity.Category;
import com.dyzzw.blog.mapper.CategoryMapper;
import com.dyzzw.blog.service.ICategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-07-02
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

}
