// src/main/java/com/spikka/digifolio/service/AdminHelpRequestService.java
package com.spikka.digifolio.service;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Заглушка для модуля "запросы на помощь" — пока возвращает пустые данные.
 */
@Service
public class AdminHelpRequestService {

    /** Сколько открытых запросов на помощь */
    public long countOpenRequests() {
        return 0L;
    }

    /** Список описаний открытых запросов */
    public List<String> listOpenRequests() {
        return Collections.emptyList();
    }
}
