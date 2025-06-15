// src/main/java/com/spikka/digifolio/service/EducationService.java
package com.spikka.digifolio.service;

import com.spikka.digifolio.dto.*;
import com.spikka.digifolio.mapper.EducationMapper;
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
public class EducationService {
    private final EducationRepository educationRepo;
    private final UserRepository userRepo;
    private final EducationMapper mapper;

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('STUDENT')")
    public List<EducationDto> getMyEducation() {
        User me = findCurrentUser();
        return educationRepo.findByStudentId(me.getId()).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @PreAuthorize("hasRole('STUDENT')")
    public EducationDto create(EducationCreateDto dto) {
        User me = findCurrentUser();
        Education e = mapper.toEntity(dto);
        e.setStudent(me);
        return mapper.toDto(educationRepo.save(e));
    }

    @Transactional
    @PreAuthorize("hasRole('STUDENT')")
    public EducationDto update(Long id, EducationCreateDto dto) {
        Education e = educationRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Запись не найдена: " + id));
        if (!isCurrentUser(e.getStudent().getEmail()))
            throw new ResourceNotFoundException("Не ваше образование");
        e.setInstitution(dto.getInstitution());
        e.setCity(dto.getCity());
        e.setFromDate(dto.getFromDate());
        e.setToDate(dto.getToDate());
        return mapper.toDto(educationRepo.save(e));
    }

    @Transactional
    @PreAuthorize("hasRole('STUDENT')")
    public void delete(Long id) {
        Education e = educationRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Запись не найдена: " + id));
        if (!isCurrentUser(e.getStudent().getEmail()))
            throw new ResourceNotFoundException("Не ваше образование");
        educationRepo.delete(e);
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
