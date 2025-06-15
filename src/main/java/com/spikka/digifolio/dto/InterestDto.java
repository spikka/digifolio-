// src/main/java/com/spikka/digifolio/dto/InterestDto.java
package com.spikka.digifolio.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterestDto {
    private Long id;
    private String name;
}
