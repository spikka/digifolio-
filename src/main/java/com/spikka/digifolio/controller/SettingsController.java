package com.spikka.digifolio.controller;

import com.spikka.digifolio.dto.ProfileFormDto;
import com.spikka.digifolio.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class SettingsController {
    private final UserService userService;

    @GetMapping("/settings")
    public String settingsForm(Model model) {
        model.addAttribute("form", userService.getCurrentProfileForm());
        return "settings";
    }

    @PostMapping("/settings")
    public String saveSettings(@ModelAttribute("form") @Valid ProfileFormDto form,
                               BindingResult br) {
        if (br.hasErrors()) return "settings";
        userService.updateProfile(form);
        return "redirect:/profile";
    }
}