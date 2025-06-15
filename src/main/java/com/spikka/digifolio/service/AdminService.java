package com.spikka.digifolio.service;

import com.spikka.digifolio.dto.TeacherCreateDto;
import com.spikka.digifolio.dto.UserDto;
import com.spikka.digifolio.dto.UserEditDto;
import com.spikka.digifolio.model.Role;
import com.spikka.digifolio.model.User;
import com.spikka.digifolio.repository.RoleRepository;
import com.spikka.digifolio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;

    public List<String> getAllRoles() {
        return roleRepo.findAll().stream()
                .map(Role::getName)
                .toList();
    }

    public void setUserRole(Long userId, String roleName) {
        User user = userRepo.findById(userId).orElseThrow();
        Role role = roleRepo.findByName(roleName).orElseThrow();
        user.getRoles().clear();
        user.getRoles().add(role);
        userRepo.save(user);
    }

    public Map<String, Object> getSystemStats() {
        // Заполни актуальными данными из БД
        return Map.of(
                "userCount", userRepo.count(),
                // ...
                "achievementsCount", 0,
                "avgRating", 0,
                "studentCount", 0,
                "teacherCount", 0,
                "newAchievementsWeek", 0
        );
    }

    public void createTeacher(TeacherCreateDto form) {
        if (userRepo.findByEmail(form.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email уже зарегистрирован");
        }
        Role teacherRole = roleRepo.findByName("ROLE_TEACHER")
                .orElseThrow(() -> new RuntimeException("Роль TEACHER не найдена"));

        User u = new User();
        u.setEmail(form.getEmail());
        String password = form.getPassword();
        if (password == null || password.isBlank()) password = "teacher123";
        u.setPassword(passwordEncoder.encode(password));
        u.setFullName(form.getFullName());
        u.setFaculty(form.getFaculty());
        u.getRoles().add(teacherRole);

        userRepo.save(u);
    }

    public void updateUser(Long id, UserEditDto dto) {
        User user = userRepo.findById(id).orElseThrow();
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getFullName() != null) user.setFullName(dto.getFullName());
        if (dto.getGroupName() != null) user.setGroupName(dto.getGroupName());
        if (dto.getFaculty() != null) user.setFaculty(dto.getFaculty());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        userRepo.save(user);
    }

    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }
}
