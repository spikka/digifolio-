package com.spikka.digifolio.controller;

import com.spikka.digifolio.dto.AchievementDto;
import com.spikka.digifolio.model.AchievementType;
import com.spikka.digifolio.service.AchievementService;
import com.spikka.digifolio.service.NotificationService;
import com.spikka.digifolio.service.RecommendationService;
import com.spikka.digifolio.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/teacher")
@RequiredArgsConstructor
@PreAuthorize("hasRole('TEACHER')")
public class TeacherController {
    private final AchievementService achievementService;
    private final NotificationService notificationService;
    private final RecommendationService recommendationService;
    private final UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("notifications", notificationService.getNewAchievementsNotifications());
        return "teacher_dashboard";
    }

    @GetMapping("/achievements")
    public String achievements(
            @RequestParam(required = false) AchievementType type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) String q,
            Model model
    ) {
        List<AchievementDto> achievements = achievementService.findAllFiltered(type, from, to, tag, studentId, q);
        model.addAttribute("achievements", achievements);
        model.addAttribute("students", userService.getAllStudents());
        model.addAttribute("filterType", type);
        model.addAttribute("filterFrom", from);
        model.addAttribute("filterTo", to);
        model.addAttribute("filterTag", tag);
        model.addAttribute("filterStudentId", studentId);
        model.addAttribute("filterQ", q);
        return "achievements_teacher";
    }

    @PostMapping("/achievements/{id}/recommend")
    public String recommend(@PathVariable Long id, @RequestHeader(value = "referer", required = false) String referer) {
        recommendationService.recommend(id);
        return "redirect:" + (referer != null ? referer : "/teacher/achievements");
    }
}