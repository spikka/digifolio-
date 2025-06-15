// src/main/java/com/spikka/digifolio/dto/RatingDto.java
package com.spikka.digifolio.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingDto {
    @Min(value = 1, message = "Оценка должна быть не меньше 1")
    @Max(value = 5, message = "Оценка должна быть не больше 5")
    private int stars;
}
