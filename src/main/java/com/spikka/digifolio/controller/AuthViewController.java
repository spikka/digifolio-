// src/main/java/com/spikka/digifolio/controller/AuthViewController.java
package com.spikka.digifolio.controller;

import com.spikka.digifolio.dto.RegisterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AuthViewController {

    @GetMapping("/login")
    public String login() {
        return "login";  // src/main/resources/templates/login.html
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("register", new RegisterDto());
        return "register";  // src/main/resources/templates/register.html
    }
}
