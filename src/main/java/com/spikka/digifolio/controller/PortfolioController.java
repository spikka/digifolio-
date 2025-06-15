// src/main/java/com/spikka/digifolio/controller/PortfolioController.java
package com.spikka.digifolio.controller;

import com.spikka.digifolio.dto.AchievementDto;
import com.spikka.digifolio.dto.CommentDto;
import com.spikka.digifolio.dto.UserDto;
import com.spikka.digifolio.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/portfolio")
public class PortfolioController {
    private final UserService userService;
    private final AchievementService achievementService;
    private final RatingService ratingService;
    private final CommentService commentService;

    @GetMapping("/{id}")
    public String viewPortfolio(@PathVariable Long id, Model model) {
        UserDto user = userService.getUserById(id);
        List<AchievementDto> achievements = achievementService.findByStudentId(id);
        Double avgRating = ratingService.getAvgRatingForUser(id);
        String avgRatingStars = ratingService.renderStars(avgRating);
        List<CommentDto> comments = commentService.findLastCommentsForUser(id, 5);

        model.addAttribute("user", user);
        model.addAttribute("achievements", achievements);
        model.addAttribute("avgRating", avgRating != null ? String.format("%.2f", avgRating) : "-");
        model.addAttribute("avgRatingStars", avgRatingStars);
        model.addAttribute("comments", comments);

        return "portfolio";
    }
}
