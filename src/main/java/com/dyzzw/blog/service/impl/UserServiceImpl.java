package com.dyzzw.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dyzzw.blog.common.lang.Result;
import com.dyzzw.blog.entity.User;
import com.dyzzw.blog.mapper.UserMapper;
import com.dyzzw.blog.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dyzzw.blog.shiro.AccountProfile;
import org.apache.catalina.security.SecurityUtil;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-07-02
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public Result register(User user) {

       int count= this.count(new QueryWrapper<User>().eq("email",user.getEmail())
        .or()
        .eq("username",user.getUsername())
        );
        if(count>0){
            return Result.fail("用户名或邮箱已被占用");
        }
        User temp = new User();
        temp.setUsername(user.getUsername());
        temp.setPassword(SecureUtil.md5(user.getPassword()));
        temp.setEmail(user.getEmail());
        temp.setCreated(new Date());
        temp.setAvatar("qczbhuiem.bkt.clouddn.com/5306b7ee977f4d4c840f93724de77dc7.jpg");
        temp.setPoint(0);
        temp.setVipLevel(0);
        temp.setCommentCount(0);
        temp.setGender("0");
        temp.setPostCount(0);
        this.save(temp);
        return Result.success();
    }

    @Override
    public AccountProfile login(String email, String password) {
        User user =this.getOne(new QueryWrapper<User>().eq("email",email));
        if(user ==null){
            throw new UnknownAccountException();
        }
        if(!password.equals(user.getPassword())){
            throw new IncorrectCredentialsException();
        }
        user.setLasted(new Date());
        this.updateById(user);
        AccountProfile profile = new AccountProfile();

        BeanUtil.copyProperties(user,profile);
        return profile;
    }
}
