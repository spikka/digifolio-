// src/main/java/com/spikka/digifolio/controller/ProjectController.java
package com.spikka.digifolio.controller;

import com.spikka.digifolio.dto.ProjectCreateDto;
import com.spikka.digifolio.dto.ProjectDto;
import com.spikka.digifolio.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService service;

    @GetMapping
    public String list(Model m) {
        List<ProjectDto> items = service.getMyProjects();
        m.addAttribute("items", items);
        return "projects_list";
    }

    @GetMapping("/new")
    public String createForm(Model m) {
        m.addAttribute("form", new ProjectCreateDto());
        return "project_form";
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute("form") ProjectCreateDto form,
            BindingResult br,
            @RequestParam("file") MultipartFile file
    ) {
        if (br.hasErrors()) return "project_form";
        service.create(form, file);
        return "redirect:/projects";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model m) {
        ProjectDto d = service.getById(id);
        ProjectCreateDto form = new ProjectCreateDto(
                d.getTitle(), d.getDescription(), d.getLink()
        );
        m.addAttribute("form", form);
        m.addAttribute("id", id);
        m.addAttribute("filePath", d.getFilePath());
        return "project_form";
    }

    @PostMapping("/{id}/edit")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("form") ProjectCreateDto form,
            BindingResult br,
            @RequestParam("file") MultipartFile file,
            Model m        // <-- добавить!
    ) {
        if (br.hasErrors()) {
            m.addAttribute("filePath", service.getById(id).getFilePath());
            return "project_form";
        }
        service.update(id, form, file);
        return "redirect:/projects";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/projects";
    }
}
