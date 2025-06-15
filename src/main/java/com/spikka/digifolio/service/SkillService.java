// src/main/java/com/spikka/digifolio/service/SkillService.java
package com.spikka.digifolio.service;

import com.spikka.digifolio.dto.*;
import com.spikka.digifolio.mapper.SkillMapper;
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
public class SkillService {
    private final SkillRepository skillRepo;
    private final UserRepository userRepo;
    private final SkillMapper mapper;
    private final UserService userService;

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('STUDENT')")
    public List<SkillDto> getMySkills() {
        var me = findCurrentUser();
        return skillRepo.findByStudentId(me.getId()).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void update(Long id, SkillCreateDto dto) {
        Skill s = skillRepo.findById(id).orElseThrow();
        s.setName(dto.getName());
        skillRepo.save(s);
    }

    @Transactional(readOnly = true)
    public SkillDto getById(Long id) {
        Skill s = skillRepo.findById(id).orElseThrow();
        return mapper.toDto(s);
    }

    @Transactional
    public void create(SkillCreateDto dto) {
        User user = userService.getCurrentUser();
        Skill skill = mapper.toEntity(dto);
        skill.setStudent(user);
        skillRepo.save(skill);
    }

    @Transactional
    @PreAuthorize("hasRole('STUDENT')")
    public SkillDto addSkill(SkillCreateDto dto) {
        var me = findCurrentUser();
        Skill s = mapper.toEntity(dto);
        s.setStudent(me);
        return mapper.toDto(skillRepo.save(s));
    }

    @Transactional
    public void delete(Long id) {
        skillRepo.deleteById(id);
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
