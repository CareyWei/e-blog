package com.dyzzw.blog.service;

import com.dyzzw.blog.common.lang.Result;
import com.dyzzw.blog.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dyzzw.blog.shiro.AccountProfile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jobob
 * @since 2020-07-02
 */
public interface IUserService extends IService<User> {

    Result register(User user);

    AccountProfile login(String email, String password);
}
