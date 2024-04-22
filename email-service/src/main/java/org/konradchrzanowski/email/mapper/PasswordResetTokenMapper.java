package org.konradchrzanowski.email.mapper;

import org.konradchrzanowski.email.domain.PasswordResetToken;
import org.konradchrzanowski.email.service.dto.PasswordResetTokenDTO;
import org.konradchrzanowski.utils.mapper.EntityMapper;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PasswordResetTokenMapper implements EntityMapper<PasswordResetTokenDTO, PasswordResetToken> {
    @Override
    public PasswordResetToken toEntity(PasswordResetTokenDTO dto) {
        if (dto == null) {
            return null;
        }
        return PasswordResetToken.builder()
                .id(dto.id())
                .passwordResetToken(dto.passwordResetToken())
                .userId(dto.userId())
                .userEmail(dto.email())
                .createDate(dto.createDate())
                .expireDate(dto.expireDate())
                .confirmDate(dto.confirmDat()).build();
    }

    @Override
    public PasswordResetTokenDTO toDto(PasswordResetToken entity) {
        if (entity == null) {
            return null;
        }
        return new PasswordResetTokenDTO(
                entity.getId(),
                entity.getPasswordResetToken(),
                entity.getUserId(),
                null,
                entity.getUserEmail(),
                entity.getCreateDate(),
                entity.getExpireDate(),
                entity.getConfirmDate()
        );
    }

    @Override
    public List<PasswordResetToken> toEntity(List<PasswordResetTokenDTO> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) {
            return Collections.emptyList();
        }
        return dtoList.stream().map(this::toEntity).toList();
    }

    @Override
    public List<PasswordResetTokenDTO> toDto(List<PasswordResetToken> entityList) {
        if (entityList == null || entityList.isEmpty()) {
            return Collections.emptyList();
        }
        return entityList.stream().map(this::toDto).toList();
    }

    @Override
    public Set<PasswordResetToken> toEntity(Set<PasswordResetTokenDTO> dtoSet) {
        if(dtoSet == null || dtoSet.isEmpty()) {
            return Collections.emptySet();
        }
        return dtoSet.stream().map(this::toEntity).collect(Collectors.toSet());
    }

    @Override
    public Set<PasswordResetTokenDTO> toDto(Set<PasswordResetToken> entitySet) {
        if (entitySet == null || entitySet.isEmpty()) {
            return Collections.emptySet();
        }
        return entitySet.stream().map(this::toDto).collect(Collectors.toSet());
    }
}
