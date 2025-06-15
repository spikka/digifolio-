package com.spikka.digifolio.repository;

import com.spikka.digifolio.model.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EducationRepository extends JpaRepository<Education, Long> {
    List<Education> findByStudentId(Long studentId);
}
