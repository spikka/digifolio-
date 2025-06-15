// src/main/java/com/spikka/digifolio/dto/CertificateCreateDto.java
package com.spikka.digifolio.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateCreateDto {
    @NotBlank(message = "Название сертификата обязательно")
    private String name;

    @NotBlank(message = "Организатор обязателен")
    private String issuer;

    @NotNull(message = "Укажите дату получения")
    private LocalDate date;
}
