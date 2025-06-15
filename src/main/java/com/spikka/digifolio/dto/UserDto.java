// src/main/java/com/spikka/digifolio/dto/UserDto.java
package com.spikka.digifolio.dto;

import com.spikka.digifolio.model.Visibility;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private String fullName;
    private String groupName;
    private String faculty;
    private String avatarPath;
    private String bio;
    private Visibility visibility;
    private String phone;
    private LocalDate birth;
    private java.util.Set<String> roles;
}
