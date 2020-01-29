package com.springboot.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping("")
    public String login(@RequestParam(value="error", required=false) String error,
                        @RequestParam(value="logout", required=false) String logout,
                        Model model, Principal principal, RedirectAttributes flash){ // Principal nos permite validar

        if(principal != null) {// true = ya inicio sesion anteriormente

            flash.addFlashAttribute("info", "You have already logged in previously");
            return "redirect:/";
        }

        if(error != null) { // cuando el usuario no existe

            model.addAttribute("error", "Login error: Incorrect username or password. Please, try again!");
        }

        if(logout != null){

            model.addAttribute("success", "session closed successfully!");
        }

        return "login";
    }

}
