// src/main/java/com/spikka/digifolio/dto/WorkDto.java
package com.spikka.digifolio.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkDto {
    private Long id;
    private String company;
    private String position;
    private LocalDate fromDate;
    private LocalDate toDate;
}
