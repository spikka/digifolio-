// src/main/java/com/spikka/digifolio/dto/EducationDto.java
package com.spikka.digifolio.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EducationDto {
    private Long id;
    private String institution;
    private String city;
    private LocalDate fromDate;
    private LocalDate toDate;
}
