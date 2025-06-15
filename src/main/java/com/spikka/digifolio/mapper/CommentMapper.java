// src/main/java/com/spikka/digifolio/mapper/CommentMapper.java
package com.spikka.digifolio.mapper;

import org.springframework.stereotype.Component;
import com.spikka.digifolio.model.Comment;
import com.spikka.digifolio.dto.CommentDto;

@Component
public class CommentMapper {
    public CommentDto toDto(Comment c) {
        if (c == null) return null;
        CommentDto dto = new CommentDto();
        dto.setAuthorName(c.getAuthor().getFullName());
        dto.setText(c.getText());
        dto.setCreatedAt(c.getCreatedAt());
        return dto;
    }
}
