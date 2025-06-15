// src/main/java/com/spikka/digifolio/controller/RatingController.java
package com.spikka.digifolio.controller;

import com.spikka.digifolio.dto.RatingDto;
import com.spikka.digifolio.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/achievements/{achievementId}/ratings")
@RequiredArgsConstructor
@Validated
public class RatingController {
    private final RatingService ratingService;

    @PostMapping
    public ResponseEntity<RatingDto> add(
            @PathVariable Long achievementId,
            @Valid @RequestBody RatingDto dto
    ) {
        RatingDto saved = ratingService.addRating(achievementId, dto);
        return ResponseEntity.ok(saved);
    }
}
