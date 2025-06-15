// src/main/java/com/spikka/digifolio/mapper/RatingMapper.java
package com.spikka.digifolio.mapper;

import org.springframework.stereotype.Component;
import com.spikka.digifolio.model.Rating;
import com.spikka.digifolio.dto.RatingDto;

@Component
public class RatingMapper {
    public RatingDto toDto(Rating r) {
        if (r == null) return null;
        return new RatingDto(r.getStars());
    }
}
