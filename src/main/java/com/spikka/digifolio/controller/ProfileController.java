// src/main/java/com/spikka/digifolio/controller/ProfileController.java
package com.spikka.digifolio.controller;

import com.spikka.digifolio.dto.ProfileFormDto;
import com.spikka.digifolio.dto.UserDto;
import com.spikka.digifolio.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;
    private final AchievementService achievementService;
    private final CommentService commentService;
    private final RatingService ratingService;
    private final NotificationService notificationService;
    private final AdminHelpRequestService helpRequestService;

    @GetMapping("/profile/edit")
    public String editProfileForm(Model model) {
        ProfileFormDto form = userService.getCurrentProfileForm();
        model.addAttribute("form", form);
        return "profile_edit";
    }

    @PostMapping("/profile/edit")
    public String saveProfile(
            @ModelAttribute("form") @Valid ProfileFormDto form,
            BindingResult br
    ) {
        if (br.hasErrors()) return "profile_edit";
        userService.updateProfile(form);
        return "redirect:/profile";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        // 1) Общие атрибуты
        UserDto user = userService.getCurrentUserProfile();
        model.addAttribute("user", user);

        // 2) Определяем роль
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isStudent = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT"));
        boolean isTeacher = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_TEACHER"));
        boolean isAdmin   = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        // 3) Рендерим соответствующий view
        if (isStudent) {
            model.addAttribute("stats", Map.of(
                    "achievementsCount", achievementService.findMyAchievements().size(),
                    "recentFeedbackCount", commentService.getMyRecentCount() + ratingService.getMyRecentCount()
            ));
            return "student_profile";
        }
        if (isTeacher) {
            // для учителя считаем новые достижения и рецензии (пока нет логики подсчёта рецензий)
            var newAchievements = notificationService.getNewAchievementsNotifications();
            model.addAttribute("notifications", newAchievements);
            model.addAttribute("stats", Map.of(
                    "newAchievementsCount", newAchievements.size(),
                    "newReviewsCount", 0
            ));
            return "teacher_profile";
        }
        if (isAdmin) {
            // для админа — статистика пользователей и запросы
            model.addAttribute("stats", Map.of(
                    "userCount", userService.countAllUsers(),
                    "achievementsCount", achievementService.findAllCount(),
                    "studentCount", userService.countByRole("ROLE_STUDENT")
            ));
            model.addAttribute("helpRequests", helpRequestService.listOpenRequests());
            return "admin_profile";
        }

        // На всякий случай, если нет ролей
        return "access_denied";
    }
}
