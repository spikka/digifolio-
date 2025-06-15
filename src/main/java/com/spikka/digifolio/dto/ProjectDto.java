// src/main/java/com/spikka/digifolio/dto/ProjectDto.java
package com.spikka.digifolio.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDto {
    private Long id;
    private String title;
    private String description;
    private String link;
    private String filePath;
}
