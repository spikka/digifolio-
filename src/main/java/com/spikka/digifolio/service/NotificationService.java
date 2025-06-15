// src/main/java/com/spikka/digifolio/service/NotificationService.java
package com.spikka.digifolio.service;

import com.spikka.digifolio.model.Achievement;
import com.spikka.digifolio.repository.AchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final AchievementRepository achievementRepo;

    /**
     * Возвращает список "уведомлений" о новых достижениях, добавленных сегодня.
     */
    public List<String> getNewAchievementsNotifications() {
        LocalDate today = LocalDate.now();
        // Для этого нужно в репозитории объявить метод:
        // List<Achievement> findByDate(LocalDate date);
        List<Achievement> todays = achievementRepo.findByDate(today);
        return todays.stream()
                .map(a -> a.getStudent().getFullName() + ": «" + a.getTitle() + "»")
                .collect(Collectors.toList());
    }
}
