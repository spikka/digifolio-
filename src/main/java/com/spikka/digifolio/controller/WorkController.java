// src/main/java/com/spikka/digifolio/controller/WorkController.java
package com.spikka.digifolio.controller;

import com.spikka.digifolio.dto.WorkCreateDto;
import com.spikka.digifolio.dto.WorkDto;
import com.spikka.digifolio.service.WorkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/work")
@RequiredArgsConstructor
public class WorkController {
    private final WorkService service;

    @GetMapping
    public String list(Model m) {
        List<WorkDto> items = service.getMyWork();
        m.addAttribute("items", items);
        return "work_list";
    }

    @GetMapping("/new")
    public String createForm(Model m) {
        m.addAttribute("form", new WorkCreateDto());
        return "work_form";
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute("form") WorkCreateDto form,
            BindingResult br
    ) {
        if (br.hasErrors()) return "work_form";
        service.create(form);
        return "redirect:/work";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model m) {
        WorkDto d = service.getMyWork().stream()
                .filter(w -> w.getId().equals(id)).findFirst().orElseThrow();
        WorkCreateDto form = new WorkCreateDto(
                d.getCompany(), d.getPosition(), d.getFromDate(), d.getToDate()
        );
        m.addAttribute("form", form);
        m.addAttribute("id", id);
        return "work_form";
    }

    @PostMapping("/{id}/edit")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("form") WorkCreateDto form,
            BindingResult br
    ) {
        if (br.hasErrors()) return "work_form";
        service.update(id, form);
        return "redirect:/work";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/work";
    }
}
