// src/main/java/com/spikka/digifolio/dto/PortfolioDto.java
package com.spikka.digifolio.dto;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioDto {
    private UserDto user;
    private List<AchievementDto> achievements;
    private Double averageRating;
    private List<CommentDto> recentComments;
}
