// src/main/java/com/spikka/digifolio/dto/EducationCreateDto.java
package com.spikka.digifolio.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EducationCreateDto {
    @NotBlank(message = "Учебное заведение обязательно")
    private String institution;

    @NotBlank(message = "Город обязателен")
    private String city;

    @NotNull(message = "Дата начала обязательна")
    private LocalDate fromDate;

    private LocalDate toDate;
}
