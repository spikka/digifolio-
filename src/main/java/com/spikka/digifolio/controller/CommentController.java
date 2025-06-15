// src/main/java/com/spikka/digifolio/controller/CommentController.java
package com.spikka.digifolio.controller;

import com.spikka.digifolio.dto.CommentDto;
import com.spikka.digifolio.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/achievements/{achievementId}/comments")
@RequiredArgsConstructor
@Validated
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentDto> add(
            @PathVariable Long achievementId,
            @Valid @RequestBody CommentDto dto
    ) {
        CommentDto saved = commentService.addComment(achievementId, dto);
        return ResponseEntity.ok(saved);
    }
}
