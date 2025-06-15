// src/main/java/com/spikka/digifolio/controller/AuthController.java
package com.spikka.digifolio.controller;

import com.spikka.digifolio.dto.RegisterDto;
import com.spikka.digifolio.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute("register") RegisterDto dto,
            BindingResult br
    ) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            br.rejectValue("confirmPassword", "error.confirmPassword", "Пароли не совпадают");
        }
        if (br.hasErrors()) {
            return "register";
        }
        try {
            userService.register(dto);
        } catch (IllegalArgumentException ex) {
            br.rejectValue("email", "error.email", ex.getMessage());
            return "register";
        }
        return "redirect:/login?registered";
    }
}
