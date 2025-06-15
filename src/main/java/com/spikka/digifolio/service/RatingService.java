// src/main/java/com/spikka/digifolio/service/RatingService.java
package com.spikka.digifolio.service;

import com.spikka.digifolio.dto.RatingDto;
import com.spikka.digifolio.exception.ResourceNotFoundException;
import com.spikka.digifolio.mapper.RatingMapper;
import com.spikka.digifolio.model.Achievement;
import com.spikka.digifolio.model.Rating;
import com.spikka.digifolio.model.User;
import com.spikka.digifolio.repository.AchievementRepository;
import com.spikka.digifolio.repository.RatingRepository;
import com.spikka.digifolio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepo;
    private final AchievementRepository achRepo;
    private final UserRepository userRepo;
    private final RatingMapper ratingMapper;

    @Transactional
    @PreAuthorize("hasRole('TEACHER')")
    public RatingDto addRating(Long achId, RatingDto dto) {
        Achievement ach = achRepo.findById(achId)
                .orElseThrow(() -> new ResourceNotFoundException("Achievement not found"));
        User teacher = findCurrentUser();

        Rating r = Rating.builder()
                .achievement(ach)
                .author(teacher)
                .stars(dto.getStars())
                .createdAt(java.time.LocalDate.now())
                .build();

        Rating saved = ratingRepo.save(r);
        return ratingMapper.toDto(saved);
    }

    private User findCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public Double getAvgRatingForAchievement(Long achId) {
        Achievement ach = achRepo.findById(achId)
                .orElseThrow(() -> new ResourceNotFoundException("Achievement not found"));
        List<Rating> ratings = ratingRepo.findByAchievement(ach);
        if (ratings.isEmpty()) return null;
        return ratings.stream().mapToInt(Rating::getStars).average().orElse(0);
    }

    public Double getAvgRatingForUser(Long userId) {
        // Средний рейтинг по всем достижениям пользователя
        List<Long> achievementIds = achRepo.findIdsByStudentId(userId);
        if (achievementIds.isEmpty()) return null;
        Double avg = ratingRepo.avgRatingByAchievementIds(achievementIds);
        return avg;
    }

    // Helper (примерный вариант — можно реализовать прямо в шаблоне)
    public String renderStars(Double avg) {
        if (avg == null) return "☆☆☆☆☆";
        int full = avg.intValue();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < full; i++) sb.append("★");
        for (int i = full; i < 5; i++) sb.append("☆");
        return sb.toString();
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('STUDENT')")
    public long getMyRecentCount() {
        Long me = findCurrentUser().getId();
        return ratingRepo.countByAchievementStudentId(me);
    }
}
