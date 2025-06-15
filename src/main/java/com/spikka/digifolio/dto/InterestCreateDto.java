// src/main/java/com/spikka/digifolio/dto/InterestCreateDto.java
package com.spikka.digifolio.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterestCreateDto {
    @NotBlank(message = "Название интереса обязательно")
    private String name;
}
