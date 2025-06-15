// src/main/java/com/spikka/digifolio/mapper/WorkMapper.java
package com.spikka.digifolio.mapper;

import org.springframework.stereotype.Component;
import com.spikka.digifolio.model.Work;
import com.spikka.digifolio.dto.*;

@Component
public class WorkMapper {
    public WorkDto toDto(Work w) {
        if (w == null) return null;
        return WorkDto.builder()
                .id(w.getId())
                .company(w.getCompany())
                .position(w.getPosition())
                .fromDate(w.getFromDate())
                .toDate(w.getToDate())
                .build();
    }
    public Work toEntity(WorkCreateDto dto) {
        if (dto == null) return null;
        Work w = new Work();
        w.setCompany(dto.getCompany());
        w.setPosition(dto.getPosition());
        w.setFromDate(dto.getFromDate());
        w.setToDate(dto.getToDate());
        return w;
    }
}
