package org.konradchrzanowski.email.mapper;

import org.konradchrzanowski.email.domain.SentEmail;
import org.konradchrzanowski.email.service.dto.SentEmailDTO;
import org.konradchrzanowski.utils.mapper.EntityMapper;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class SentEmailMapper implements EntityMapper<SentEmailDTO, SentEmail> {
    @Override
    public SentEmail toEntity(SentEmailDTO dto) {
        if (dto == null) {
            return null;
        }
        return SentEmail.builder()
                .id(dto.id())
                .title(dto.title())
                .content(dto.content())
                .mailEvent(dto.mailEvent())
                .language(dto.language())
                .createDatetime(dto.createDatetime())
                .userId(dto.userId())
                .build();
    }

    @Override
    public SentEmailDTO toDto(SentEmail entity) {
        if (entity == null) {
            return null;
        }
        return new SentEmailDTO(
                entity.getId(),
                entity.getUserId(),
                entity.getUserEmail(),
                entity.getTitle(),
                entity.getContent(),
                entity.getMailEvent(),
                entity.getLanguage(),
                entity.getCreateDatetime()
        );
    }

    @Override
    public List<SentEmail> toEntity(List<SentEmailDTO> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) {
            return List.of();
        }
        return dtoList.stream().map(this::toEntity).toList();
    }

    @Override
    public List<SentEmailDTO> toDto(List<SentEmail> entityList) {
        if (entityList == null || entityList.isEmpty()) {
            return List.of();
        }
        return entityList.stream().map(this::toDto).toList();
    }
}
