// src/main/java/com/spikka/digifolio/service/ProjectService.java
package com.spikka.digifolio.service;

import com.spikka.digifolio.dto.ProjectCreateDto;
import com.spikka.digifolio.dto.ProjectDto;
import com.spikka.digifolio.exception.ResourceNotFoundException;
import com.spikka.digifolio.mapper.ProjectMapper;
import com.spikka.digifolio.model.Project;
import com.spikka.digifolio.model.User;
import com.spikka.digifolio.repository.ProjectRepository;
import com.spikka.digifolio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projRepo;
    private final ProjectMapper mapper;
    private final UserRepository userRepo;
    private final FileStorageService fileStorageService;

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('STUDENT')")
    public List<ProjectDto> getMyProjects() {
        User me = getCurrentUser();
        return projRepo.findByStudentId(me.getId()).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('STUDENT')")
    public ProjectDto getById(Long id) {
        Project p = projRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Проект не найден: " + id));
        if (!p.getStudent().getId().equals(getCurrentUser().getId())) {
            throw new ResourceNotFoundException("Это не ваш проект");
        }
        return mapper.toDto(p);
    }

    @Transactional
    @PreAuthorize("hasRole('STUDENT')")
    public ProjectDto create(ProjectCreateDto dto, MultipartFile file) {
        User me = getCurrentUser();
        Project p = mapper.toEntity(dto);
        p.setStudent(me);
        if (file != null && !file.isEmpty()) {
            String path = fileStorageService.store(file);
            p.setFilePath(path);
        }
        Project saved = projRepo.save(p);
        return mapper.toDto(saved);
    }

    @Transactional
    @PreAuthorize("hasRole('STUDENT')")
    public ProjectDto update(Long id, ProjectCreateDto dto, MultipartFile file) {
        Project p = projRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Проект не найден: " + id));
        if (!p.getStudent().getId().equals(getCurrentUser().getId())) {
            throw new ResourceNotFoundException("Это не ваш проект");
        }
        p.setTitle(dto.getTitle());
        p.setDescription(dto.getDescription());
        p.setLink(dto.getLink());
        if (file != null && !file.isEmpty()) {
            String path = fileStorageService.store(file);
            p.setFilePath(path);
        }
        Project updated = projRepo.save(p);
        return mapper.toDto(updated);
    }

    @Transactional
    @PreAuthorize("hasRole('STUDENT')")
    public void delete(Long id) {
        Project p = projRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Проект не найден: " + id));
        if (!p.getStudent().getId().equals(getCurrentUser().getId())) {
            throw new ResourceNotFoundException("Это не ваш проект");
        }
        projRepo.delete(p);
    }

    private User getCurrentUser() {
        String email = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));
    }
}
