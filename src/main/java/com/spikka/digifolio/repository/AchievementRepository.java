package com.spikka.digifolio.repository;

import com.spikka.digifolio.model.Achievement;
import com.spikka.digifolio.model.AchievementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    // Все достижения студента
    List<Achievement> findByStudentId(Long studentId);

    // Фильтрация по типу
    List<Achievement> findByType(AchievementType type);

    // По дате
    List<Achievement> findByDate(LocalDate date);

    // По тегам (если tags хранится CSV)
    List<Achievement> findByTagsContaining(String tag);

    @Query("SELECT a.id FROM Achievement a WHERE a.student.id = :studentId")
    List<Long> findIdsByStudentId(@Param("studentId") Long studentId);

}
