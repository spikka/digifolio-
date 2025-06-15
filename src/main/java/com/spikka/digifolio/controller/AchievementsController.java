// src/main/java/com/spikka/digifolio/controller/AchievementsController.java
package com.spikka.digifolio.controller;

import com.spikka.digifolio.dto.AchievementCreateDto;
import com.spikka.digifolio.dto.AchievementDto;
import com.spikka.digifolio.model.AchievementType;
import com.spikka.digifolio.service.AchievementService;
import com.spikka.digifolio.service.CommentService;
import com.spikka.digifolio.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/achievements")
@RequiredArgsConstructor
public class AchievementsController {

    private final AchievementService achievementService;
    private final RatingService ratingService;
    private final CommentService commentService;

    /**
     * Список собственных достижений с фильтрами.
     */
    @GetMapping
    public String list(
            @RequestParam(required = false) AchievementType type,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) String tag,
            Model model
    ) {
        List<AchievementDto> achievements = achievementService
                .findMyAchievementsFiltered(type, from, to, tag);
        model.addAttribute("achievements", achievements);
        model.addAttribute("filterType", type);
        model.addAttribute("filterFrom", from);
        model.addAttribute("filterTo", to);
        model.addAttribute("filterTag", tag);
        return "achievements_list";
    }

    /**
     * Форма создания нового достижения.
     */
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("form", new AchievementCreateDto());
        return "achievement_create";
    }

    /**
     * Обработка создания.
     */
    @PostMapping
    public String create(
            @Valid @ModelAttribute("form") AchievementCreateDto form,
            BindingResult br,
            @RequestParam("file") MultipartFile file,
            Model model
    ) {
        if (br.hasErrors()) {
            return "achievement_create";
        }
        achievementService.create(form, file);
        return "redirect:/achievements";
    }

    /**
     * Форма редактирования существующего достижения.
     */
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        AchievementDto dto = achievementService.getById(id);
        AchievementCreateDto form = new AchievementCreateDto(
                dto.getId(), dto.getTitle(), dto.getDescription(),
                dto.getType(), dto.getDate(), dto.getTags()
        );
        model.addAttribute("form", form);
        model.addAttribute("filePath", dto.getFilePath());
        return "achievement_edit";
    }

    /**
     * Обработка обновления.
     */
    @PostMapping("/{id}/edit")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("form") AchievementCreateDto form,
            BindingResult br,
            @RequestParam("file") MultipartFile file,
            Model model
    ) {
        if (br.hasErrors()) {
            model.addAttribute("filePath", achievementService.getById(id).getFilePath());
            return "achievement_edit";
        }
        achievementService.update(id, form, file);

        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isTeacher = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_TEACHER"));

        if (isAdmin || isTeacher) {
            return "redirect:/admin/achievements";
        }
        return "redirect:/achievements";
    }

    /**
     * Удаление достижения.
     */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        achievementService.delete(id);

        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isTeacher = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_TEACHER"));

        if (isAdmin || isTeacher) {
            return "redirect:/admin/achievements";
        }
        return "redirect:/achievements";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        AchievementDto ach = achievementService.getById(id);
        model.addAttribute("achievement", ach);
        model.addAttribute("avgRating", ratingService.getAvgRatingForAchievement(id));
        model.addAttribute("comments", commentService.findCommentsForAchievement(id));
        model.addAttribute("ratingForm", new com.spikka.digifolio.dto.RatingDto());
        model.addAttribute("commentForm", new com.spikka.digifolio.dto.CommentDto());
        return "achievement_view";
    }

    @PostMapping("/{id}/rate")
    public String rate(@PathVariable Long id, @ModelAttribute("ratingForm") com.spikka.digifolio.dto.RatingDto form) {
        ratingService.addRating(id, form);
        return "redirect:/achievements/" + id;
    }

    @PostMapping("/{id}/comment")
    public String comment(@PathVariable Long id, @ModelAttribute("commentForm") com.spikka.digifolio.dto.CommentDto form) {
        commentService.addComment(id, form);
        return "redirect:/achievements/" + id;
    }
}
