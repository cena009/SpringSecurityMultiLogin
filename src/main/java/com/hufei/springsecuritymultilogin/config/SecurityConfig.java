package com.hufei.springsecuritymultilogin.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.PrintWriter;
import java.util.Date;

/**
 * <p>
 * </p>
 *
 * @Author: hufei
 * @Date: 2021/06/1/21:20
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider;
    @Autowired
    private WxAuthenticationProvider wxAuthenticationProvider;
    @Autowired
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
    @Autowired
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(usernamePasswordAuthenticationProvider);
        auth.authenticationProvider(wxAuthenticationProvider);
    }


    @Bean
    VueLoginAuthenticationFilter vueLoginAuthenticationFilter() throws Exception {
        VueLoginAuthenticationFilter vueLoginAuthenticationFilter = new VueLoginAuthenticationFilter();
        vueLoginAuthenticationFilter.setAuthenticationSuccessHandler(myAuthenticationSuccessHandler);
        vueLoginAuthenticationFilter.setAuthenticationFailureHandler(myAuthenticationFailureHandler);
        vueLoginAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
        vueLoginAuthenticationFilter.setFilterProcessesUrl("/vueLogin");
        return vueLoginAuthenticationFilter;
    }

    @Bean
    WxLoginAuthenticationFilter wxLoginAuthenticationFilter() throws Exception {
        WxLoginAuthenticationFilter wxLoginAuthenticationFilter = new WxLoginAuthenticationFilter();
        wxLoginAuthenticationFilter.setAuthenticationSuccessHandler((request, response, authentication) -> {
            long exp = System.currentTimeMillis() + 1000 * 60 * 60;//60分钟
            String jwtToken = Jwts.builder()
                    .setId(authentication.getCredentials().toString())//唯一编号
                    .setSubject(authentication.getName())
                    .setIssuedAt(new Date())//设置签发时间
                    .setExpiration(new Date(exp))//过期时间
                    .signWith(SignatureAlgorithm.HS256, "hufei@hufei.com").compact();

            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write(new ObjectMapper().writeValueAsString(jwtToken));
            out.flush();
            out.close();
        });
        wxLoginAuthenticationFilter.setAuthenticationFailureHandler(myAuthenticationFailureHandler);
        wxLoginAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
        wxLoginAuthenticationFilter.setFilterProcessesUrl("/wxLogin");
        return wxLoginAuthenticationFilter;
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .logout()
                .logoutSuccessHandler((req, resp, authentication) -> {
                            resp.setContentType("application/json;charset=utf-8");
                            PrintWriter out = resp.getWriter();
                            out.write(new ObjectMapper().writeValueAsString("注销成功!"));
                            out.flush();
                            out.close();
                        }
                )
                .permitAll()
                .and()
                .csrf().disable().exceptionHandling();
        http.addFilterAt(vueLoginAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        http.addFilterBefore(new JwtFilter(), VueLoginAuthenticationFilter.class);
        http.addFilterAt(wxLoginAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
