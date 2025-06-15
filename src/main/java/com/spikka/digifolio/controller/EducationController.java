// src/main/java/com/spikka/digifolio/controller/EducationController.java
package com.spikka.digifolio.controller;

import com.spikka.digifolio.dto.EducationCreateDto;
import com.spikka.digifolio.dto.EducationDto;
import com.spikka.digifolio.service.EducationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/education")
@RequiredArgsConstructor
public class EducationController {
    private final EducationService service;

    @GetMapping
    public String list(Model m) {
        List<EducationDto> items = service.getMyEducation();
        m.addAttribute("items", items);
        return "education_list";
    }

    @GetMapping("/new")
    public String createForm(Model m) {
        m.addAttribute("form", new EducationCreateDto());
        return "education_form";
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute("form") EducationCreateDto form,
            BindingResult br
    ) {
        if (br.hasErrors()) return "education_form";
        service.create(form);
        return "redirect:/education";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model m) {
        EducationDto d = service.getMyEducation().stream()
                .filter(e -> e.getId().equals(id))
                .findFirst().orElseThrow();
        EducationCreateDto form = new EducationCreateDto(
                d.getInstitution(), d.getCity(), d.getFromDate(), d.getToDate()
        );
        m.addAttribute("form", form);
        m.addAttribute("id", id);
        return "education_form";
    }

    @PostMapping("/{id}/edit")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("form") EducationCreateDto form,
            BindingResult br
    ) {
        if (br.hasErrors()) return "education_form";
        service.update(id, form);
        return "redirect:/education";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/education";
    }
}
