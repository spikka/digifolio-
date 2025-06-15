// src/main/java/com/spikka/digifolio/dto/SkillDto.java
package com.spikka.digifolio.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkillDto {
    private Long id;
    private String name;
}
