// src/main/java/com/spikka/digifolio/service/InterestService.java
package com.spikka.digifolio.service;

import com.spikka.digifolio.dto.*;
import com.spikka.digifolio.mapper.InterestMapper;
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
public class InterestService {
    private final InterestRepository interestRepo;
    private final UserRepository userRepo;
    private final InterestMapper mapper;
    private final UserService userService;

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('STUDENT')")
    public List<InterestDto> getMyInterests() {
        var me = findCurrentUser();
        return interestRepo.findByStudentId(me.getId()).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void create(InterestCreateDto dto) {
        User user = userService.getCurrentUser();
        Interest interest = mapper.toEntity(dto);
        interest.setStudent(user);
        interestRepo.save(interest);
    }

    @Transactional(readOnly = true)
    public InterestDto getById(Long id) {
        Interest i = interestRepo.findById(id).orElseThrow();
        return mapper.toDto(i);
    }

    @Transactional
    public void update(Long id, InterestCreateDto dto) {
        Interest i = interestRepo.findById(id).orElseThrow();
        i.setName(dto.getName());
        interestRepo.save(i);
    }

    @Transactional
    public void delete(Long id) {
        interestRepo.deleteById(id);
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
