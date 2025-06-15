// src/main/java/com/spikka/digifolio/service/PortfolioService.java
package com.spikka.digifolio.service;

import com.spikka.digifolio.dto.*;
import com.spikka.digifolio.mapper.AchievementMapper;
import com.spikka.digifolio.mapper.CommentMapper;
import com.spikka.digifolio.mapper.UserMapper;
import com.spikka.digifolio.model.*;
import com.spikka.digifolio.repository.*;
import com.spikka.digifolio.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.*;

@Service
@RequiredArgsConstructor
public class PortfolioService {
    private final UserRepository userRepo;
    private final AchievementRepository achRepo;
    private final RatingRepository ratingRepo;
    private final CommentRepository commentRepo;
    private final UserMapper userMapper;
    private final AchievementMapper achMapper;
    private final CommentMapper commentMapper;

    @Transactional(readOnly = true)
    public PortfolioDto getPortfolio(Long userId) {
        // 1) Юзер
        User u = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        UserDto userDto = userMapper.toDto(u);

        // 2) Достижения
        List<Achievement> list = achRepo.findByStudentId(userId);
        List<AchievementDto> achDtos = list.stream()
                .map(achMapper::toDto)
                .collect(Collectors.toList());

        // 3) Средний рейтинг по всем достижениям
        Double avg = list.stream()
                .flatMap(a -> ratingRepo.findByAchievement(a).stream())
                .mapToInt(Rating::getStars)
                .average()
                .orElse(0.0);

        // 4) Последние 5 комментариев
        List<CommentDto> recent = list.stream()
                .flatMap(a -> commentRepo.findTop5ByAchievementOrderByCreatedAtDesc(a).stream())
                .limit(5)
                .map(commentMapper::toDto)
                .collect(Collectors.toList());

        return PortfolioDto.builder()
                .user(userDto)
                .achievements(achDtos)
                .averageRating(avg)
                .recentComments(recent)
                .build();
    }
}
