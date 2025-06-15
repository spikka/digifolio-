// src/main/java/com/spikka/digifolio/service/CertificateService.java
package com.spikka.digifolio.service;

import com.spikka.digifolio.dto.CertificateCreateDto;
import com.spikka.digifolio.dto.CertificateDto;
import com.spikka.digifolio.exception.ResourceNotFoundException;
import com.spikka.digifolio.mapper.CertificateMapper;
import com.spikka.digifolio.model.Certificate;
import com.spikka.digifolio.model.User;
import com.spikka.digifolio.repository.CertificateRepository;
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
public class CertificateService {
    private final CertificateRepository certRepo;
    private final CertificateMapper mapper;
    private final UserRepository userRepo;
    private final FileStorageService fileStorageService;

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('STUDENT')")
    public List<CertificateDto> getMyCertificates() {
        User me = getCurrentUser();
        return certRepo.findByStudentId(me.getId()).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('STUDENT')")
    public CertificateDto getById(Long id) {
        Certificate c = certRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Сертификат не найден: " + id));
        if (!c.getStudent().getId().equals(getCurrentUser().getId())) {
            throw new ResourceNotFoundException("Это не ваш сертификат");
        }
        return mapper.toDto(c);
    }

    @Transactional
    @PreAuthorize("hasRole('STUDENT')")
    public CertificateDto create(CertificateCreateDto dto, MultipartFile file) {
        User me = getCurrentUser();
        Certificate c = mapper.toEntity(dto);
        c.setStudent(me);
        if (file != null && !file.isEmpty()) {
            String path = fileStorageService.store(file);
            c.setFilePath(path);
        }
        Certificate saved = certRepo.save(c);
        return mapper.toDto(saved);
    }

    @Transactional
    @PreAuthorize("hasRole('STUDENT')")
    public CertificateDto update(Long id, CertificateCreateDto dto, MultipartFile file) {
        Certificate c = certRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Сертификат не найден: " + id));
        if (!c.getStudent().getId().equals(getCurrentUser().getId())) {
            throw new ResourceNotFoundException("Это не ваш сертификат");
        }
        c.setName(dto.getName());
        c.setIssuer(dto.getIssuer());
        c.setDate(dto.getDate());
        if (file != null && !file.isEmpty()) {
            String path = fileStorageService.store(file);
            c.setFilePath(path);
        }
        Certificate updated = certRepo.save(c);
        return mapper.toDto(updated);
    }

    @Transactional
    @PreAuthorize("hasRole('STUDENT')")
    public void delete(Long id) {
        Certificate c = certRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Сертификат не найден: " + id));
        if (!c.getStudent().getId().equals(getCurrentUser().getId())) {
            throw new ResourceNotFoundException("Это не ваш сертификат");
        }
        certRepo.delete(c);
    }

    private User getCurrentUser() {
        String email = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));
    }
}
