// src/main/java/com/spikka/digifolio/mapper/SkillMapper.java
package com.spikka.digifolio.mapper;

import org.springframework.stereotype.Component;
import com.spikka.digifolio.model.Skill;
import com.spikka.digifolio.dto.*;

@Component
public class SkillMapper {
    public SkillDto toDto(Skill s) {
        if (s == null) return null;
        return SkillDto.builder()
                .id(s.getId())
                .name(s.getName())
                .build();
    }
    public Skill toEntity(SkillCreateDto dto) {
        if (dto == null) return null;
        Skill s = new Skill();
        s.setName(dto.getName());
        return s;
    }
}
