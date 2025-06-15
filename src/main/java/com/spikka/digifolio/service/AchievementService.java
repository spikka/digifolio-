// src/main/java/com/spikka/digifolio/service/AchievementService.java
package com.spikka.digifolio.service;

import com.spikka.digifolio.dto.AchievementCreateDto;
import com.spikka.digifolio.dto.AchievementDto;
import com.spikka.digifolio.exception.AccessDeniedException;
import com.spikka.digifolio.exception.ResourceNotFoundException;
import com.spikka.digifolio.mapper.AchievementMapper;
import com.spikka.digifolio.model.Achievement;
import com.spikka.digifolio.model.AchievementType;
import com.spikka.digifolio.model.User;
import com.spikka.digifolio.repository.AchievementRepository;
import com.spikka.digifolio.repository.UserRepository;
import com.spikka.digifolio.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AchievementService {
    private final AchievementRepository achRepo;
    private final UserRepository userRepo;
    private final AchievementMapper achMapper;
    private final FileStorageService fileStorageService;

    // Новый метод для фильтрации
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('STUDENT')")
    public List<AchievementDto> findMyAchievementsFiltered(
            AchievementType type,
            LocalDate from,
            LocalDate to,
            String tag
    ) {
        User me = findCurrentUser();
        return achRepo.findByStudentId(me.getId()).stream()
                .filter(a -> type == null || a.getType() == type)
                .filter(a -> from == null || !a.getDate().isBefore(from))
                .filter(a -> to   == null || !a.getDate().isAfter(to))
                .filter(a -> tag  == null || tag.isBlank() ||
                        (a.getTags() != null && a.getTags().contains(tag)))
                .map(achMapper::toDto)
                .collect(Collectors.toList());
    }

    // Создание с файлом
    @Transactional
    @PreAuthorize("hasRole('STUDENT')")
    public AchievementDto create(AchievementCreateDto dto, MultipartFile file) {
        User student = findCurrentUser();
        Achievement entity = achMapper.toEntity(dto);
        entity.setStudent(student);

        if (file != null && !file.isEmpty()) {
            String filename = fileStorageService.store(file);
            entity.setFilePath(filename);
        }

        Achievement saved = achRepo.save(entity);
        return achMapper.toDto(saved);
    }

    // Обновление с файлом
    @Transactional
    @PreAuthorize("hasRole('STUDENT') or hasRole('TEACHER') or hasRole('ADMIN')")
    public AchievementDto update(Long id, AchievementCreateDto dto, MultipartFile file) {
        Achievement existing = achRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Achievement not found: " + id));
        boolean owner = isCurrentUser(existing.getStudent().getEmail());
        boolean elevated = hasRole("ROLE_ADMIN") || hasRole("ROLE_TEACHER");
        if (!owner && !elevated) {
            throw new AccessDeniedException("Not your achievement");
        }

        existing.setTitle(dto.getTitle());
        existing.setDescription(dto.getDescription());
        existing.setType(dto.getType());
        existing.setDate(dto.getDate());
        existing.setTags(dto.getTags());

        if (file != null && !file.isEmpty()) {
            String filename = fileStorageService.store(file);
            existing.setFilePath(filename);
        }

        Achievement saved = achRepo.save(existing);
        return achMapper.toDto(saved);
    }

    // Получить по id
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('STUDENT') or hasRole('TEACHER') or hasRole('ADMIN')")
    public AchievementDto getById(Long id) {
        Achievement a = achRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Achievement not found: " + id));
        return achMapper.toDto(a);
    }

    // Удаление
    @Transactional
    @PreAuthorize("hasRole('STUDENT') or hasRole('TEACHER') or hasRole('ADMIN')")
    public void delete(Long id) {
        Achievement existing = achRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Achievement not found: " + id));
        boolean owner = isCurrentUser(existing.getStudent().getEmail());
        boolean elevated = hasRole("ROLE_ADMIN") || hasRole("ROLE_TEACHER");
        if (!owner && !elevated) {
            throw new AccessDeniedException("Not your achievement");
        }
        achRepo.delete(existing);
    }

    // Вспомогательные
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

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('STUDENT')")
    public List<AchievementDto> findMyAchievements() {
        // просто делегируем фильтру без параметров
        return findMyAchievementsFiltered(null, null, null, null);
    }

    public List<AchievementDto> findByStudentId(Long studentId) {
        return achRepo.findByStudentId(studentId).stream()
                .map(achMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public long findAllCount() {
        return achRepo.count();
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public List<AchievementDto> findAllFiltered(
            AchievementType type,
            LocalDate from,
            LocalDate to,
            String tag,
            Long studentId,
            String query
    ) {
        return achRepo.findAll().stream()
                .filter(a -> studentId == null || a.getStudent().getId().equals(studentId))
                .filter(a -> type == null || a.getType() == type)
                .filter(a -> from == null || (a.getDate() != null && !a.getDate().isBefore(from)))
                .filter(a -> to == null || (a.getDate() != null && !a.getDate().isAfter(to)))
                .filter(a -> tag == null || tag.isBlank() || (a.getTags() != null && a.getTags().contains(tag)))
                .filter(a -> query == null || query.isBlank() ||
                        (a.getTitle() != null && a.getTitle().toLowerCase().contains(query.toLowerCase())) ||
                        (a.getDescription() != null && a.getDescription().toLowerCase().contains(query.toLowerCase())) ||
                        (a.getTags() != null && a.getTags().toLowerCase().contains(query.toLowerCase())))
                .map(achMapper::toDto)
                .collect(Collectors.toList());
    }

    private boolean hasRole(String role) {
        return SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(role));
    }
}
