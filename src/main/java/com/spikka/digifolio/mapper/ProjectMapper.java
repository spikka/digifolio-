// src/main/java/com/spikka/digifolio/mapper/ProjectMapper.java
package com.spikka.digifolio.mapper;

import com.spikka.digifolio.dto.ProjectCreateDto;
import com.spikka.digifolio.dto.ProjectDto;
import com.spikka.digifolio.model.Project;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

    public ProjectDto toDto(Project p) {
        if (p == null) return null;
        return ProjectDto.builder()
                .id(p.getId())
                .title(p.getTitle())
                .description(p.getDescription())
                .link(p.getLink())
                .filePath(p.getFilePath())
                .build();
    }

    public Project toEntity(ProjectCreateDto dto) {
        if (dto == null) return null;
        Project p = new Project();
        p.setTitle(dto.getTitle());
        p.setDescription(dto.getDescription());
        p.setLink(dto.getLink());
        return p;
    }

    public void update(Project entity, ProjectCreateDto dto) {
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setLink(dto.getLink());
    }
}
