// src/main/java/com/spikka/digifolio/dto/TeacherCreateDto.java
package com.spikka.digifolio.dto;

import lombok.Data;

@Data
public class TeacherCreateDto {
    private String email;
    private String fullName;
    private String faculty;
    private String password; // это поле здесь допустимо!
}
