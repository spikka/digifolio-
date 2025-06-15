// src/main/java/com/spikka/digifolio/service/UserService.java
package com.spikka.digifolio.service;

import com.spikka.digifolio.dto.ProfileFormDto;
import com.spikka.digifolio.dto.RegisterDto;
import com.spikka.digifolio.dto.UserDto;
import com.spikka.digifolio.exception.ResourceNotFoundException;
import com.spikka.digifolio.mapper.UserMapper;
import com.spikka.digifolio.model.Role;
import com.spikka.digifolio.model.User;
import com.spikka.digifolio.model.Visibility;
import com.spikka.digifolio.repository.RoleRepository;
import com.spikka.digifolio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Transactional
    public void register(RegisterDto dto) {
        if (userRepo.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email уже зарегистрирован");
        }
        Role studentRole = roleRepo.findByName("ROLE_STUDENT")
                .orElseThrow(() -> new ResourceNotFoundException("Роль STUDENT не найдена"));
        User u = new User();
        u.setEmail(dto.getEmail());
        u.setPassword(passwordEncoder.encode(dto.getPassword()));
        u.setVisibility(Visibility.PUBLIC);
        u.getRoles().add(studentRole);
        // остальные поля (fullName, bio и т.п.) можно заполнить позже
        userRepo.save(u);
    }

    @Transactional(readOnly = true)
    public long countByRole(String roleName) {
        return userRepo.countByRoleName(roleName);
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAllStudents() {
        return userRepo.findAll().stream()
                .filter(u -> u.getRoles().stream().anyMatch(r -> r.getName().equals("ROLE_STUDENT")))
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserDto getCurrentUserProfile() {
        User u = findCurrentUser();
        return userMapper.toDto(u);
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepo.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserDto> searchStudents(String query) {
        return userRepo.findAll().stream()
                .filter(u -> u.getRoles().stream().anyMatch(r -> r.getName().equals("ROLE_STUDENT")))
                .filter(u -> query == null || query.isBlank() ||
                        (u.getFullName() != null && u.getFullName().toLowerCase().contains(query.toLowerCase())))
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @PreAuthorize("hasAnyRole('STUDENT','TEACHER')")
    public UserDto updateProfile(ProfileFormDto dto) {
        User user = getCurrentUser();
        user.setFullName(dto.getFullName());
        user.setGroupName(dto.getGroupName());
        user.setFaculty(dto.getFaculty());
        user.setBio(dto.getBio());
        user.setPhone(dto.getPhone());
        user.setBirth(dto.getBirth());
        user.setVisibility(dto.getVisibility());
        User saved = userRepo.save(user);
        return userMapper.toDto(saved);
    }

    private User findCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional(readOnly = true)
    public long countAllUsers() {
        return userRepo.count();
    }

    // Получить профиль для редактирования (заполнить форму)
    public ProfileFormDto getCurrentProfileForm() {
        User user = getCurrentUser();
        ProfileFormDto dto = new ProfileFormDto();
        dto.setFullName(user.getFullName());
        dto.setGroupName(user.getGroupName());
        dto.setFaculty(user.getFaculty());
        dto.setBio(user.getBio());
        dto.setPhone(user.getPhone());
        dto.setBirth(user.getBirth());
        dto.setVisibility(user.getVisibility()); // если DTO.visibility == enum Visibility
        return dto;
    }

    @Transactional(readOnly = true)
    public long countRegistrationsLast24h() {
        // если в будущем добавите поле createdAt, замените на userRepo.countByCreatedAtAfter(...)
        return 0L;
    }

    public UserDto getUserById(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.toDto(user);
    }

    // Найти пользователя по email (username) из security context
    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + email));
    }
}
