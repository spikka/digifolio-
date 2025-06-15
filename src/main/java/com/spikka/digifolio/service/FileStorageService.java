// src/main/java/com/spikka/digifolio/service/FileStorageService.java
package com.spikka.digifolio.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path uploadDir;

    public FileStorageService(@Value("${uploads.path}") String uploadPath) {
        this.uploadDir = Paths.get(uploadPath).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.uploadDir);
        } catch (IOException ex) {
            throw new RuntimeException("Не удалось создать директорию для загрузок: " + uploadDir, ex);
        }
    }

    /**
     * Сохраняет файл в файловую систему и возвращает сгенерированное имя
     */
    public String store(MultipartFile file) {
        String original = StringUtils.cleanPath(file.getOriginalFilename());
        String ext = "";
        int idx = original.lastIndexOf('.');
        if (idx > 0) {
            ext = original.substring(idx);
        }
        String filename = UUID.randomUUID().toString() + ext;
        try {
            Path target = uploadDir.resolve(filename);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return filename;
        } catch (IOException ex) {
            throw new RuntimeException("Не удалось сохранить файл: " + original, ex);
        }
    }

    /**
     * Загружает файл как Spring Resource по его имени
     */
    public Resource loadAsResource(String filename) {
        try {
            Path file = uploadDir.resolve(filename).normalize();
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Файл не найден или не доступен: " + filename);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("Ошибка при загрузке файла: " + filename, ex);
        }
    }
}
