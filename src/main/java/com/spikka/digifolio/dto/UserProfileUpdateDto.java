// src/main/java/com/spikka/digifolio/dto/UserProfileUpdateDto.java
package com.spikka.digifolio.dto;

import com.spikka.digifolio.model.Visibility;
import lombok.*;

import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileUpdateDto {
    @NotBlank @Size(min = 2)
    private String fullName;

    private String groupName;
    private String faculty;

    @Size(max = 1000)
    private String bio;

    @NotNull
    private Visibility visibility;
}
