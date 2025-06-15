package com.spikka.digifolio.controller;

import com.spikka.digifolio.dto.SkillCreateDto;
import com.spikka.digifolio.dto.SkillDto;
import com.spikka.digifolio.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/skills")
@RequiredArgsConstructor
public class SkillController {
    private final SkillService service;

    @GetMapping
    public String list(Model m) {
        List<SkillDto> items = service.getMySkills();
        m.addAttribute("items", items);
        return "skills_list";
    }

    @GetMapping("/new")
    public String createForm(Model m) {
        m.addAttribute("form", new SkillCreateDto());
        return "skill_form";
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute("form") SkillCreateDto form,
            BindingResult br
    ) {
        if (br.hasErrors()) return "skill_form";
        service.create(form);
        return "redirect:/skills";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model m) {
        SkillDto d = service.getById(id);
        SkillCreateDto form = new SkillCreateDto(d.getName());
        m.addAttribute("form", form);
        m.addAttribute("id", id);
        return "skill_form";
    }

    @PostMapping("/{id}/edit")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("form") SkillCreateDto form,
            BindingResult br
    ) {
        if (br.hasErrors()) return "skill_form";
        service.update(id, form);
        return "redirect:/skills";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/skills";
    }
}
