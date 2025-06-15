// src/main/java/com/spikka/digifolio/dto/CertificateDto.java
package com.spikka.digifolio.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificateDto {
    private Long id;
    private String name;
    private String issuer;
    private LocalDate date;
    private String filePath;
}
