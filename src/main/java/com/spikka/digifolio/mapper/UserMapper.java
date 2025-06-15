// src/main/java/com/spikka/digifolio/mapper/UserMapper.java
package com.spikka.digifolio.mapper;

import org.springframework.stereotype.Component;
import com.spikka.digifolio.model.User;
import com.spikka.digifolio.dto.UserDto;
import com.spikka.digifolio.dto.ProfileFormDto;

import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDto toDto(User u) {
        if (u == null) return null;
        return UserDto.builder()
                .id(u.getId())
                .email(u.getEmail())
                .fullName(u.getFullName())
                .groupName(u.getGroupName())
                .faculty(u.getFaculty())
                .avatarPath(u.getAvatarPath())
                .bio(u.getBio())
                .visibility(u.getVisibility())
                .phone(u.getPhone())           // новую строку
                .birth(u.getBirth())           // и эту
                .roles(u.getRoles().stream()
                        .map(r -> r.getName())
                        .collect(Collectors.toSet()))
                .build();
    }

    public void updateEntity(User entity, ProfileFormDto dto) {
        if (dto.getFullName() != null)   entity.setFullName(dto.getFullName());
        if (dto.getGroupName() != null)  entity.setGroupName(dto.getGroupName());
        if (dto.getFaculty() != null)    entity.setFaculty(dto.getFaculty());
        if (dto.getBio() != null)        entity.setBio(dto.getBio());
        if (dto.getPhone() != null)      entity.setPhone(dto.getPhone());
        if (dto.getBirth() != null)      entity.setBirth(dto.getBirth());
        if (dto.getVisibility() != null) entity.setVisibility(dto.getVisibility());
    }
}
