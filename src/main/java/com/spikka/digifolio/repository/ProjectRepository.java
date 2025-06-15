package com.spikka.digifolio.repository;

import com.spikka.digifolio.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByStudentId(Long studentId);
}
