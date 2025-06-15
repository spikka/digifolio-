// src/main/java/com/spikka/digifolio/mapper/AchievementMapper.java
package com.spikka.digifolio.mapper;

import com.spikka.digifolio.dto.*;
import com.spikka.digifolio.model.Achievement;
import org.springframework.stereotype.Component;

@Component
public class AchievementMapper {
    public AchievementDto toDto(Achievement a) {
        if (a == null) return null;
        return AchievementDto.builder()
                .id(a.getId())
                .studentId(a.getStudent().getId())
                .studentName(a.getStudent().getFullName())
                .title(a.getTitle())
                .description(a.getDescription())
                .type(a.getType())
                .date(a.getDate())
                .tags(a.getTags())
                .filePath(a.getFilePath())
                .build();
    }

    public Achievement toEntity(AchievementCreateDto dto) {
        Achievement a = new Achievement();
        a.setTitle(dto.getTitle());
        a.setDescription(dto.getDescription());
        a.setType(dto.getType());
        a.setDate(dto.getDate());
        a.setTags(dto.getTags());
        return a;
    }
}
