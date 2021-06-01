package com.hufei.springsecuritymultilogin.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * <p>
 * </p>
 *
 * @Author: hufei
 * @Date: 2021/05/31/21:06
 */
@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        Object principal = authentication.getPrincipal();
        float initialCapacity = 3 / 0.75F + 1.0F;
        HashMap<String, Object> respMap = new HashMap<>((int) initialCapacity);
        respMap.put("code", 200);
        respMap.put("message", "success");
        respMap.put("data", principal);
        String s = new ObjectMapper().writeValueAsString(respMap);
        out.write(s);
        out.flush();
        out.close();
    }
}
