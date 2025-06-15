// src/main/java/com/spikka/digifolio/dto/AchievementCreateDto.java
package com.spikka.digifolio.dto;

import com.spikka.digifolio.model.AchievementType;
import lombok.*;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor  // этот конструктор теперь будет принимать ВСЕ поля: id, title, description, type, date, tags
public class AchievementCreateDto {
    private Long id;  // ← добавили

    @NotBlank(message = "Название обязательно")
    private String title;

    @Size(max = 2000, message = "Описание слишком длинное")
    private String description;

    @NotNull(message = "Выберите тип достижения")
    private AchievementType type;

    @NotNull(message = "Укажите дату")
    private LocalDate date;

    private String tags; // CSV
}
