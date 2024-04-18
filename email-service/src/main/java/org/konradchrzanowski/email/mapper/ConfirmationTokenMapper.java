package org.konradchrzanowski.email.mapper;

import org.konradchrzanowski.email.domain.ConfirmationToken;
import org.konradchrzanowski.email.service.dto.ConfirmationTokenDTO;
import org.konradchrzanowski.utils.mapper.EntityMapper;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class ConfirmationTokenMapper implements EntityMapper<ConfirmationTokenDTO, ConfirmationToken> {
    @Override
    public ConfirmationToken toEntity(ConfirmationTokenDTO dto) {
        if (dto == null) {
            return null;
        }
        return ConfirmationToken.builder()
                .id(dto.id())
                .confirmationToken(dto.confirmationToken())
                .userId(dto.userId())
                .userEmail(dto.email())
                .createDate(dto.createDate())
                .expireDate(dto.expireDate())
                .confirmDate(dto.confirmDat()).build();
    }

    @Override
    public ConfirmationTokenDTO toDto(ConfirmationToken entity) {
        if (entity == null) {
            return null;
        }
        return new ConfirmationTokenDTO(
                entity.getId(),
                entity.getConfirmationToken(),
                entity.getUserId(),
                null,
                entity.getUserEmail(),
                entity.getCreateDate(),
                entity.getExpireDate(),
                entity.getConfirmDate()
        );
    }

    @Override
    public List<ConfirmationToken> toEntity(List<ConfirmationTokenDTO> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) {
            return Collections.emptyList();
        }
        return dtoList.stream().map(this::toEntity).toList();
    }

    @Override
    public List<ConfirmationTokenDTO> toDto(List<ConfirmationToken> entityList) {
        if (entityList == null || entityList.isEmpty()) {
            return Collections.emptyList();
        }
        return entityList.stream().map(this::toDto).toList();
    }
}
