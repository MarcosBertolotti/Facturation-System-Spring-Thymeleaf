package com.springboot.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LocaleController {

    @GetMapping("/locale")
    public String locale(HttpServletRequest request) {

        String lastUrl = request.getHeader("referer"); // referer = nos entrega la referencia (link) de la ultima url

        return "redirect:" + lastUrl;
    }
}
