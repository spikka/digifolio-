// src/main/java/com/spikka/digifolio/mapper/EducationMapper.java
package com.spikka.digifolio.mapper;

import org.springframework.stereotype.Component;
import com.spikka.digifolio.model.Education;
import com.spikka.digifolio.dto.*;

@Component
public class EducationMapper {
    public EducationDto toDto(Education e) {
        if (e == null) return null;
        return EducationDto.builder()
                .id(e.getId())
                .institution(e.getInstitution())
                .city(e.getCity())
                .fromDate(e.getFromDate())
                .toDate(e.getToDate())
                .build();
    }
    public Education toEntity(EducationCreateDto dto) {
        if (dto == null) return null;
        Education e = new Education();
        e.setInstitution(dto.getInstitution());
        e.setCity(dto.getCity());
        e.setFromDate(dto.getFromDate());
        e.setToDate(dto.getToDate());
        return e;
    }
}
