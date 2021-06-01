package com.hufei.springsecuritymultilogin.config;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * <p>
 *     自定义认证处理器
 * </p>
 *
 * @Author: hufei
 * @Date: 2021/05/31/00:39
 */
@Component
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();
        String password = authentication.getCredentials().toString();
        if (!"admin".equals(userName) || !"123456".equals(password)) {
            throw new BadCredentialsException("username or password authentication fail");
        }
        //安全起见密码不要返回
        return new UsernamePasswordAuthenticationToken(userName, null, Collections.emptyList());

    }

    @Override
    public boolean supports(Class<?> aClass) {
        /**
         * providerManager会遍历所有
         * SecurityConfig中注册的provider集合
         * 根据此方法返回true或false来决定由哪个provider
         * 去校验请求过来的authentication
         */
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(aClass));

    }
}
