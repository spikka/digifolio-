package com.spikka.digifolio.dto;

import com.spikka.digifolio.model.Visibility;
import lombok.*;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileFormDto {
    @NotBlank(message = "ФИО обязательно")
    @Pattern(
            regexp = "^[А-ЯЁ][а-яё]+\\s+[А-ЯЁ][а-яё]+(\\s+[А-ЯЁ][а-яё]+)*$",
            message = "ФИО: только кириллица, минимум два слова"
    )
    private String fullName;

    private String groupName;
    private String faculty;

    @Size(max = 1000, message = "Bio слишком длинное")
    private String bio;

    @Pattern(
            regexp = "^\\+?\\d{10,15}$",
            message = "Телефон: 10–15 цифр, можно с +"
    )
    private String phone;

    @Past(message = "Дата рождения должна быть в прошлом")
    private LocalDate birth;

    @NotNull(message = "Выберите видимость")
    private Visibility visibility;
}
