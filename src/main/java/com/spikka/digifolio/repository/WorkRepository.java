package com.spikka.digifolio.repository;

import com.spikka.digifolio.model.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WorkRepository extends JpaRepository<Work, Long> {
    List<Work> findByStudentId(Long studentId);
}
