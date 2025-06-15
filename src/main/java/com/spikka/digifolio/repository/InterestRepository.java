package com.spikka.digifolio.repository;

import com.spikka.digifolio.model.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InterestRepository extends JpaRepository<Interest, Long> {
    List<Interest> findByStudentId(Long studentId);
}
