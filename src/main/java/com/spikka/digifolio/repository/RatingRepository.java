package com.spikka.digifolio.repository;

import com.spikka.digifolio.model.Rating;
import com.spikka.digifolio.model.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    // Все оценки для достижения
    List<Rating> findByAchievement(Achievement achievement);

    long countByAchievementStudentId(Long studentId);

    @Query("SELECT AVG(r.stars) FROM Rating r WHERE r.achievement.id IN :ids")
    Double avgRatingByAchievementIds(@Param("ids") List<Long> achievementIds);
}
