package com.dyzzw.blog.controller;

import cn.hutool.crypto.SecureUtil;
import com.dyzzw.blog.common.lang.Result;
import com.dyzzw.blog.entity.User;
import com.dyzzw.blog.util.ValidationUtil;
import com.google.code.kaptcha.Producer;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
public class AuthController extends BaseController {
    private  static final  String KAPTCHA_SESSION_KEY ="KAPTCHA_SESSION_KEY";
    @Autowired
    Producer producer;

    /**
     * 验证码生成
     * @param rep
     * @throws IOException
     */
    @GetMapping("/capthca.jpg")
    public void kaptcha(HttpServletResponse rep) throws IOException {
        //验证码
        String text = producer.createText();
        BufferedImage bufferedImage  = producer.createImage(text) ;
        rep.setHeader("Cache-Control","no-store,no-cache");
        req.getSession().setAttribute("KAPTCHA_SESSION_KEY",text);
        rep.setContentType("image/jpeg");
        ServletOutputStream outputStream = rep.getOutputStream();
        ImageIO.write(bufferedImage,"jpeg",outputStream);



    }

   @GetMapping("/login")
    public String login(){
        return "/auth/login";
    }

    @ResponseBody
    @PostMapping("/login")
    public Result doLogin(String email,String password){
        if((email == null) || (password == null)){
            return Result.fail("用户名密码不能为空");
        }
        UsernamePasswordToken token = new UsernamePasswordToken(email, SecureUtil.md5(password));
        try {
            SecurityUtils.getSubject().login(token);

        } catch (AuthenticationException e) {
            if (e instanceof UnknownAccountException) {
                return Result.fail("用户不存在");
            } else if (e instanceof LockedAccountException) {
                return Result.fail("用户被禁用");
            } else if (e instanceof IncorrectCredentialsException) {
                return Result.fail("密码错误");
            } else {
                return Result.fail("用户认证失败");
            }
        }



        return Result.success().action("/");
    }

    @GetMapping("/register")
    public String register(){
        return "/auth/reg";
    }

    /**
     * 注册校验
     * @param user
     * @param repassword
     * @param vercode
     * @return
     */
    @ResponseBody
    @PostMapping("/register")
    public Result doRegister(User user,String repassword,String vercode){
        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(user);
        if(validResult.hasErrors()){
            return Result.fail(validResult.getErrors());
        }
        if(!user.getPassword().equals(repassword)){
            return Result.fail("两次输入密码不相同");
        }

        String capthca = (String) req.getSession().getAttribute("KAPTCHA_SESSION_KEY");
        System.out.println(capthca);
        if(vercode==null|| !(vercode.equalsIgnoreCase(capthca))){
            return Result.fail("验证码不正确");
        }

        //完成注册
       Result result = userService.register(user);

        return Result.success().action("/login");
    }
    /**
     * 退出
     */
    @GetMapping("/user/logout")
    public String logout(){
        SecurityUtils.getSubject().logout();
        return "redirect:/";
    }
}
