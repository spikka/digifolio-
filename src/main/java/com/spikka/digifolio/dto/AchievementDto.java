package com.spikka.digifolio.dto;

import com.spikka.digifolio.model.AchievementType;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AchievementDto {
    private Long id;
    private Long studentId;
    private String studentName;
    private String title;
    private String description;
    private AchievementType type;
    private LocalDate date;
    private String tags;
    private String filePath;

    public boolean isImage() {
        if (filePath == null) return false;
        String lower = filePath.toLowerCase();
        return lower.endsWith(".jpg") || lower.endsWith(".jpeg") ||
                lower.endsWith(".png") || lower.endsWith(".gif");
    }

    public String getTypeRus() {
        if (type == null) return "";
        return switch (type) {
            case ACADEMIC -> "Учебное";
            case SPORT    -> "Спортивное";
            case SOCIAL   -> "Социальное";
            case PROJECT  -> "Проект";
            case OTHER    -> "Другое";
            default       -> type.name();
        };
    }
}
