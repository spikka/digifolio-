// src/main/java/com/spikka/digifolio/service/WorkService.java
package com.spikka.digifolio.service;

import com.spikka.digifolio.dto.*;
import com.spikka.digifolio.mapper.WorkMapper;
import com.spikka.digifolio.model.*;
import com.spikka.digifolio.repository.*;
import com.spikka.digifolio.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkService {
    private final WorkRepository workRepo;
    private final UserRepository userRepo;
    private final WorkMapper mapper;

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('STUDENT')")
    public List<WorkDto> getMyWork() {
        User me = findCurrentUser();
        return workRepo.findByStudentId(me.getId()).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @PreAuthorize("hasRole('STUDENT')")
    public WorkDto create(WorkCreateDto dto) {
        User me = findCurrentUser();
        Work w = mapper.toEntity(dto);
        w.setStudent(me);
        return mapper.toDto(workRepo.save(w));
    }

    @Transactional
    @PreAuthorize("hasRole('STUDENT')")
    public WorkDto update(Long id, WorkCreateDto dto) {
        Work w = workRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Запись не найдена: " + id));
        if (!isCurrentUser(w.getStudent().getEmail()))
            throw new ResourceNotFoundException("Не ваш опыт работы");
        w.setCompany(dto.getCompany());
        w.setPosition(dto.getPosition());
        w.setFromDate(dto.getFromDate());
        w.setToDate(dto.getToDate());
        return mapper.toDto(workRepo.save(w));
    }

    @Transactional
    @PreAuthorize("hasRole('STUDENT')")
    public void delete(Long id) {
        Work w = workRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Запись не найдена: " + id));
        if (!isCurrentUser(w.getStudent().getEmail()))
            throw new ResourceNotFoundException("Не ваш опыт работы");
        workRepo.delete(w);
    }

    private User findCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
    private boolean isCurrentUser(String email) {
        return SecurityContextHolder.getContext()
                .getAuthentication().getName().equals(email);
    }
}
