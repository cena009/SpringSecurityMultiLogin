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
public class WxAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String code = authentication.getName();
        //调用微信登陆接口登陆成功自动生产token
        //登陆成功后返回的openId
        String openId = "openId";
        return new WxAuthenticationToken(code, openId, Collections.emptyList());

    }

    @Override
    public boolean supports(Class<?> aClass) {
        /**
         * providerManager会遍历所有
         * SecurityConfig中注册的provider集合
         * 根据此方法返回true或false来决定由哪个provider
         * 去校验请求过来的authentication
         */
        return (WxAuthenticationToken.class.isAssignableFrom(aClass));

    }
}
