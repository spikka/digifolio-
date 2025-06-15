package com.spikka.digifolio.controller;

import com.spikka.digifolio.dto.AchievementDto;
import com.spikka.digifolio.dto.TeacherCreateDto;
import com.spikka.digifolio.dto.UserDto;
import com.spikka.digifolio.dto.UserEditDto;
import com.spikka.digifolio.model.AchievementType;
import com.spikka.digifolio.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final AdminService adminService;
    private final AchievementService achievementService;
    private final CommentService commentService;
    private final RatingService ratingService;

    @GetMapping("/users")
    public String users(Model m) {
        List<UserDto> users = userService.getAllUsers();
        m.addAttribute("users", users);
        m.addAttribute("allRoles", adminService.getAllRoles()); // ["ROLE_STUDENT", ...]
        return "users_admin";
    }

    @PostMapping("/users/{id}/roles")
    public String updateRole(@PathVariable Long id, @RequestParam String role) {
        adminService.setUserRole(id, role);
        return "redirect:/admin/users";
    }

    @GetMapping("/reports")
    public String reports(Model m) {
        m.addAttribute("stats", adminService.getSystemStats());
        return "reports";
    }

    // + форма добавления учителя
    @GetMapping("/teachers/new")
    public String newTeacherForm(Model m) {
        m.addAttribute("form", new TeacherCreateDto());
        return "teacher_form";
    }

    @PostMapping("/teachers/new")
    public String createTeacher(@ModelAttribute("form") TeacherCreateDto form) {
        adminService.createTeacher(form);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/{id}/edit")
    public String editForm(@PathVariable Long id, Model m) {
        UserDto user = userService.getUserById(id);
        UserEditDto form = new UserEditDto();
        form.setId(user.getId());
        form.setEmail(user.getEmail());
        form.setFullName(user.getFullName());
        form.setGroupName(user.getGroupName());
        form.setFaculty(user.getFaculty());
        m.addAttribute("form", form);
        return "user_edit";
    }

    @PostMapping("/users/{id}/edit")
    public String updateUser(@PathVariable Long id, @ModelAttribute("form") UserEditDto form) {
        adminService.updateUser(id, form);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/achievements")
    public String achievements(
            @RequestParam(required = false) AchievementType type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) String q,
            Model m
    ) {
        List<AchievementDto> achievements = achievementService.findAllFiltered(type, from, to, tag, studentId, q);
        m.addAttribute("achievements", achievements);
        m.addAttribute("filterType", type);
        m.addAttribute("filterFrom", from);
        m.addAttribute("filterTo", to);
        m.addAttribute("filterTag", tag);
        m.addAttribute("filterStudentId", studentId);
        m.addAttribute("filterQ", q);
        m.addAttribute("students", userService.getAllStudents());
        return "achievements_admin";
    }
}
