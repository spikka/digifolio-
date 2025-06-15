// src/main/java/com/spikka/digifolio/dto/WorkCreateDto.java
package com.spikka.digifolio.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkCreateDto {
    @NotBlank(message = "Компания обязательна")
    private String company;

    @NotBlank(message = "Должность обязательна")
    private String position;

    @NotNull(message = "Дата начала обязательна")
    private LocalDate fromDate;

    private LocalDate toDate;
}
