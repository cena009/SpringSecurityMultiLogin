package com.hufei.springsecuritymultilogin.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * <p>
 * </p>
 *
 * @Author: hufei
 * @Date: 2021/06/01/13:14
 */
public class JwtFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwtToken = request.getHeader("authorization");
        if (null != jwtToken) {
            try {
                Claims claims = Jwts.parser().setSigningKey("hufei@hufei.com").parseClaimsJws(jwtToken.replace("Bearer", "")).getBody();
                String code = claims.getSubject();
                String openId = claims.getId();
                WxAuthenticationToken wxAuthenticationToken = new WxAuthenticationToken(code, openId);
                SecurityContextHolder.getContext().setAuthentication(wxAuthenticationToken);
            } catch (Exception e) {
                SecurityContextHolder.getContext().setAuthentication(null);
            }

        }

        filterChain.doFilter(request, servletResponse);
    }
}
