// src/main/java/com/spikka/digifolio/dto/ProjectCreateDto.java
package com.spikka.digifolio.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectCreateDto {
    @NotBlank(message = "Название проекта обязательно")
    private String title;

    @Size(max = 2000, message = "Описание слишком длинное")
    private String description;

    @NotBlank(message = "Ссылка на проект обязательна")
    private String link;
}
