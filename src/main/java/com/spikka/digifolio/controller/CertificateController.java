// src/main/java/com/spikka/digifolio/controller/CertificateController.java
package com.spikka.digifolio.controller;

import com.spikka.digifolio.dto.CertificateCreateDto;
import com.spikka.digifolio.dto.CertificateDto;
import com.spikka.digifolio.service.CertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/certificates")
@RequiredArgsConstructor
public class CertificateController {
    private final CertificateService service;

    @GetMapping
    public String list(Model m) {
        List<CertificateDto> items = service.getMyCertificates();
        m.addAttribute("items", items);
        return "certificates_list";
    }

    @GetMapping("/new")
    public String createForm(Model m) {
        m.addAttribute("form", new CertificateCreateDto());
        return "certificate_form";
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute("form") CertificateCreateDto form,
            BindingResult br,
            @RequestParam("file") MultipartFile file
    ) {
        if (br.hasErrors()) return "certificate_form";
        service.create(form, file);
        return "redirect:/certificates";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model m) {
        CertificateDto d = service.getById(id);
        CertificateCreateDto form = new CertificateCreateDto(
                d.getName(), d.getIssuer(), d.getDate()
        );
        m.addAttribute("form", form);
        m.addAttribute("id", id);
        m.addAttribute("filePath", d.getFilePath());
        return "certificate_form";
    }

    @PostMapping("/{id}/edit")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("form") CertificateCreateDto form,
            BindingResult br,
            @RequestParam("file") MultipartFile file,
            Model m        // <-- добавить!
    ) {
        if (br.hasErrors()) {
            // чтобы показать старый файл
            m.addAttribute("filePath", service.getById(id).getFilePath());
            return "certificate_form";
        }
        service.update(id, form, file);
        return "redirect:/certificates";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/certificates";
    }
}
