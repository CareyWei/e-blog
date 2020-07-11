package com.dyzzw.blog.shiro;

import com.dyzzw.blog.service.IUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountRealm extends AuthorizingRealm {

    @Autowired
    IUserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        AccountProfile profile  = (AccountProfile) principals.getPrimaryPrincipal();
        if(profile.getId()==6){
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            info.addRole("admin");
            return info;
        }
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken token1 = (UsernamePasswordToken) token;

        AccountProfile accountProfile= userService.login(token1.getUsername(),String.valueOf(token1.getPassword()));

        SecurityUtils.getSubject().getSession().setAttribute("profile",accountProfile);

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(accountProfile,token.getCredentials(),getName());

        return info;
    }
}
