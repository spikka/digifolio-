package com.spikka.digifolio.controller;

import com.spikka.digifolio.dto.InterestCreateDto;
import com.spikka.digifolio.dto.InterestDto;
import com.spikka.digifolio.service.InterestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/interests")
@RequiredArgsConstructor
public class InterestController {
    private final InterestService service;

    @GetMapping
    public String list(Model m) {
        List<InterestDto> items = service.getMyInterests();
        m.addAttribute("items", items);
        return "interests_list";
    }

    @GetMapping("/new")
    public String createForm(Model m) {
        m.addAttribute("form", new InterestCreateDto());
        return "interest_form";
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute("form") InterestCreateDto form,
            BindingResult br
    ) {
        if (br.hasErrors()) return "interest_form";
        service.create(form);
        return "redirect:/interests";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model m) {
        InterestDto d = service.getById(id);
        InterestCreateDto form = new InterestCreateDto(d.getName());
        m.addAttribute("form", form);
        m.addAttribute("id", id);
        return "interest_form";
    }

    @PostMapping("/{id}/edit")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("form") InterestCreateDto form,
            BindingResult br
    ) {
        if (br.hasErrors()) return "interest_form";
        service.update(id, form);
        return "redirect:/interests";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/interests";
    }
}
