// src/main/java/com/spikka/digifolio/service/CommentService.java
package com.spikka.digifolio.service;

import com.spikka.digifolio.dto.CommentDto;
import com.spikka.digifolio.exception.ResourceNotFoundException;
import com.spikka.digifolio.mapper.CommentMapper;
import com.spikka.digifolio.model.Achievement;
import com.spikka.digifolio.model.Comment;
import com.spikka.digifolio.model.User;
import com.spikka.digifolio.repository.AchievementRepository;
import com.spikka.digifolio.repository.CommentRepository;
import com.spikka.digifolio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepo;
    private final AchievementRepository achRepo;
    private final UserRepository userRepo;
    private final CommentMapper commentMapper;

    @Transactional
    @PreAuthorize("hasRole('TEACHER')")
    public CommentDto addComment(Long achId, CommentDto dto) {
        Achievement ach = achRepo.findById(achId)
                .orElseThrow(() -> new ResourceNotFoundException("Achievement not found"));
        User teacher = findCurrentUser();

        Comment c = Comment.builder()
                .achievement(ach)
                .author(teacher)
                .text(dto.getText())
                .createdAt(java.time.LocalDate.now())
                .build();

        Comment saved = commentRepo.save(c);
        return commentMapper.toDto(saved);
    }

    public List<CommentDto> findLastCommentsForUser(Long userId, int limit) {
        List<Long> achievementIds = achRepo.findIdsByStudentId(userId);
        if (achievementIds.isEmpty()) return List.of();
        return commentRepo.findTopNByAchievementIdsOrderByCreatedAtDesc(achievementIds, PageRequest.of(0, limit)).stream()
                .map(commentMapper::toDto)
                .toList();
    }

    public List<CommentDto> findCommentsForAchievement(Long achId) {
        Achievement ach = achRepo.findById(achId)
                .orElseThrow(() -> new ResourceNotFoundException("Achievement not found"));
        return commentRepo.findTop5ByAchievementOrderByCreatedAtDesc(ach).stream()
                .map(commentMapper::toDto)
                .toList();
    }

    private User findCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('STUDENT')")
    public long getMyRecentCount() {
        Long me = findCurrentUser().getId();
        return commentRepo.countByAchievementStudentId(me);
    }
}
