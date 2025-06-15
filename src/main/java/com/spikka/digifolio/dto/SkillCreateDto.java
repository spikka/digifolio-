// src/main/java/com/spikka/digifolio/dto/SkillCreateDto.java
package com.spikka.digifolio.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillCreateDto {
    @NotBlank(message = "Название навыка обязательно")
    private String name;
}
