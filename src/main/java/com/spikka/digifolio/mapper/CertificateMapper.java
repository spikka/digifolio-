// src/main/java/com/spikka/digifolio/mapper/CertificateMapper.java
package com.spikka.digifolio.mapper;

import com.spikka.digifolio.dto.CertificateCreateDto;
import com.spikka.digifolio.dto.CertificateDto;
import com.spikka.digifolio.model.Certificate;
import org.springframework.stereotype.Component;

@Component
public class CertificateMapper {

    public CertificateDto toDto(Certificate c) {
        if (c == null) return null;
        return CertificateDto.builder()
                .id(c.getId())
                .name(c.getName())
                .issuer(c.getIssuer())
                .date(c.getDate())
                .filePath(c.getFilePath())
                .build();
    }

    public Certificate toEntity(CertificateCreateDto dto) {
        if (dto == null) return null;
        Certificate c = new Certificate();
        c.setName(dto.getName());
        c.setIssuer(dto.getIssuer());
        c.setDate(dto.getDate());
        return c;
    }

    public void update(Certificate entity, CertificateCreateDto dto) {
        entity.setName(dto.getName());
        entity.setIssuer(dto.getIssuer());
        entity.setDate(dto.getDate());
    }
}
