package com.springboot.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Locale;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private MessageSource messageSource;

    @GetMapping("")
    public String login(@RequestParam(value="error", required=false) String error,
                        @RequestParam(value="logout", required=false) String logout,
                        Model model, Principal principal, RedirectAttributes flash, Locale locale){ // Principal nos permite validar

        if(principal != null) {// true = ya inicio sesion anteriormente
            flash.addFlashAttribute("info", messageSource.getMessage("text.login.already", null, locale));
            return "redirect:/";
        }

        if(error != null) { // cuando el usuario no existe
            model.addAttribute("error", messageSource.getMessage("text.login.error", null, locale));
        }

        if(logout != null){
            model.addAttribute("success", messageSource.getMessage("text.login.logout", null, locale));
        }
        return "login";
    }

}
