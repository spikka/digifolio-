// src/main/java/com/spikka/digifolio/mapper/InterestMapper.java
package com.spikka.digifolio.mapper;

import org.springframework.stereotype.Component;
import com.spikka.digifolio.model.Interest;
import com.spikka.digifolio.dto.*;

@Component
public class InterestMapper {
    public InterestDto toDto(Interest i) {
        if (i == null) return null;
        return InterestDto.builder()
                .id(i.getId())
                .name(i.getName())
                .build();
    }
    public Interest toEntity(InterestCreateDto dto) {
        if (dto == null) return null;
        Interest i = new Interest();
        i.setName(dto.getName());
        return i;
    }
}
