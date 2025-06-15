// src/main/java/com/spikka/digifolio/dto/CommentDto.java
package com.spikka.digifolio.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private String authorName;

    @NotBlank(message = "Текст комментария не может быть пустым")
    @Size(max = 1000, message = "Комментарий слишком длинный (макс. 1000 знаков)")
    private String text;

    private java.time.LocalDate createdAt;
}
