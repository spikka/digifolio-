package com.spikka.digifolio.controller;

import com.spikka.digifolio.dto.UserDto;
import com.spikka.digifolio.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final UserService userService;

    @GetMapping("/")             // корень теперь ведёт сразу в профиль
    public String root() {
        return "redirect:/profile";
    }
}
