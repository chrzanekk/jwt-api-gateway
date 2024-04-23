package org.konradchrzanowski.email.service.impl;

import org.konradchrzanowski.email.exception.ObjectNotFoundException;
import org.konradchrzanowski.email.repository.PasswordResetTokenRepository;
import org.konradchrzanowski.email.service.PasswordResetTokenService;
import org.konradchrzanowski.email.service.dto.PasswordResetTokenDTO;
import org.konradchrzanowski.utils.common.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    private static final int TOKEN_VALIDITY_TIME_IN_MINUTES = 30;

    private final Logger log = LoggerFactory.getLogger(PasswordResetTokenServiceImpl.class);
    private final PasswordResetTokenMapper passwordResetTokenMapper;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public PasswordResetTokenServiceImpl(PasswordResetTokenMapper passwordResetTokenMapper,
                                         PasswordResetTokenRepository passwordResetTokenRepository) {
        this.passwordResetTokenMapper = passwordResetTokenMapper;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    @Override
    public String generate() {
        log.debug("Request to generate password reset token.");
        return UUID.randomUUID().toString();
    }

    @Override
    public PasswordResetTokenDTO save(String token, UserDTO userDTO) {
        log.debug("Request to save token to reset password: {},{}", token, userDTO.getEmail());
        PasswordResetTokenDTO passwordResetTokenDTO = new PasswordResetTokenDTO(
                null,
                token,
                userDTO.getId(),
                userDTO.getUsername(),
                userDTO.getEmail(),
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(TOKEN_VALIDITY_TIME_IN_MINUTES),
                null);
        PasswordResetToken passwordResetToken = passwordResetTokenMapper.toEntity(passwordResetTokenDTO);
        PasswordResetToken saved = passwordResetTokenRepository.save(passwordResetToken);
        return passwordResetTokenMapper.toDto(saved);
    }

    @Override
    public PasswordResetTokenDTO update(PasswordResetTokenDTO passwordResetTokenDTO) {
        log.debug("Request to update password reset token: {}", passwordResetTokenDTO.passwordResetToken());
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.save(
                passwordResetTokenMapper.toEntity(new PasswordResetTokenDTO(
                        passwordResetTokenDTO.id(),
                        passwordResetTokenDTO.passwordResetToken(),
                        passwordResetTokenDTO.userId(),
                        passwordResetTokenDTO.userName(),
                        passwordResetTokenDTO.email(),
                        passwordResetTokenDTO.createDate(),
                        passwordResetTokenDTO.expireDate(),
                        LocalDateTime.now()
                )));
        return passwordResetTokenMapper.toDto(passwordResetToken);
    }

    @Override
    public PasswordResetTokenDTO get(String token) {
        log.debug("Request to get password reset token data: {}", token);
        return passwordResetTokenRepository.findByPasswordResetToken(token).map(passwordResetTokenMapper::toDto)
                .orElseThrow(() -> new ObjectNotFoundException("Token not found"));
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete password reset token: {}", id);
        passwordResetTokenRepository.deleteById(id);
    }
}
