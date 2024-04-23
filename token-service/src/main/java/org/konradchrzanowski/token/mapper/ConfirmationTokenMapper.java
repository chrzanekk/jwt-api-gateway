package org.konradchrzanowski.token.mapper;


import org.konradchrzanowski.token.domain.ConfirmationToken;
import org.konradchrzanowski.utils.common.dto.ConfirmationTokenDTO;
import org.konradchrzanowski.utils.mapper.EntityMapper;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
                .confirmDate(dto.confirmDate()).build();
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

    @Override
    public Set<ConfirmationToken> toEntity(Set<ConfirmationTokenDTO> dtoSet) {
        if(dtoSet == null || dtoSet.isEmpty()) {
            return Collections.emptySet();
        }
        return dtoSet.stream().map(this::toEntity).collect(Collectors.toSet());
    }

    @Override
    public Set<ConfirmationTokenDTO> toDto(Set<ConfirmationToken> entitySet) {
        if (entitySet == null || entitySet.isEmpty()) {
            return Collections.emptySet();
        }
        return entitySet.stream().map(this::toDto).collect(Collectors.toSet());
    }
}
