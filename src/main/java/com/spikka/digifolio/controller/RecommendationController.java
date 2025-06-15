package com.spikka.digifolio.controller;

import com.spikka.digifolio.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Validated
public class RecommendationController {
    private final RecommendationService recommendationService;

    @PostMapping("/recommendations")
    public String recommend(@RequestParam Long achievementId,
                            @RequestHeader(value = "referer", required = false) String ref) {
        recommendationService.recommend(achievementId);
        return "redirect:" + (ref != null ? ref : "/portfolio");
    }
}