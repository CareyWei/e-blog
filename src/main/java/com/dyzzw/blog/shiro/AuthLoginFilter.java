package com.dyzzw.blog.shiro;

import cn.hutool.json.JSONUtil;
import com.dyzzw.blog.common.lang.Result;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


public class AuthLoginFilter extends UserFilter {

    /**
     * 若是登录超时，重新定到登录，
     * shiro过滤器
     * @param request
     * @param response
     * @throws IOException
     */
    @Override
    protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
        //ajax弹窗显示未登录
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String header = httpServletRequest.getHeader("X-Requested-With");
        if(header != null && "XMLHttpRequest".equals(header)){
            boolean authenticated = SecurityUtils.getSubject().isAuthenticated();
            if(!authenticated){
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().println(JSONUtil.toJsonStr(Result.fail("请先登录")));
            }

        }else{
            //web重定向到登录页面
            super.redirectToLogin(request, response);
        }




    }
}
