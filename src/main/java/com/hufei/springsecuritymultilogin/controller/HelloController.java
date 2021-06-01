package com.hufei.springsecuritymultilogin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * </p>
 *
 * @Author: hufei
 * @Date: 2021/06/01/09:11
 */
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello SpringSecurity!!!";
    }
}
