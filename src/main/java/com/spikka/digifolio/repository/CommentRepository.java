package com.spikka.digifolio.repository;

import com.spikka.digifolio.model.Comment;
import com.spikka.digifolio.model.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Последние 5 комментариев к конкретному достижению
    List<Comment> findTop5ByAchievementOrderByCreatedAtDesc(Achievement achievement);

    long countByAchievementStudentId(Long studentId);

    @Query("SELECT c FROM Comment c WHERE c.achievement.id IN :ids ORDER BY c.createdAt DESC")
    List<Comment> findTopNByAchievementIdsOrderByCreatedAtDesc(@Param("ids") List<Long> achievementIds, Pageable pageable);

}
