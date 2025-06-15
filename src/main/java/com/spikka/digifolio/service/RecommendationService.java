package com.spikka.digifolio.service;

import com.spikka.digifolio.exception.ResourceNotFoundException;
import com.spikka.digifolio.model.Achievement;
import com.spikka.digifolio.model.Recommendation;
import com.spikka.digifolio.model.User;
import com.spikka.digifolio.repository.AchievementRepository;
import com.spikka.digifolio.repository.RecommendationRepository;
import com.spikka.digifolio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final RecommendationRepository recommendationRepo;
    private final AchievementRepository achievementRepo;
    private final UserRepository userRepo;

    @Transactional
    @PreAuthorize("hasRole('TEACHER')")
    public void recommend(Long achId) {
        Achievement ach = achievementRepo.findById(achId)
                .orElseThrow(() -> new ResourceNotFoundException("Achievement not found"));
        User teacher = findCurrentUser();
        boolean exists = !recommendationRepo.findByAchievementIdAndTeacherId(achId, teacher.getId()).isEmpty();
        if (exists) return;
        Recommendation rec = Recommendation.builder()
                .achievement(ach)
                .teacher(teacher)
                .createdAt(LocalDate.now())
                .build();
        recommendationRepo.save(rec);
    }

    @Transactional(readOnly = true)
    public List<Recommendation> findByTeacher(Long teacherId) {
        return recommendationRepo.findByTeacherId(teacherId);
    }

    private User findCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}