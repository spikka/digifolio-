package com.spikka.digifolio.repository;

import com.spikka.digifolio.model.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    List<Recommendation> findByTeacherId(Long teacherId);
    List<Recommendation> findByAchievementIdAndTeacherId(Long achievementId, Long teacherId);
}