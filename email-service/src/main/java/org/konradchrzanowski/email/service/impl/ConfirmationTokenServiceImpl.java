package org.konradchrzanowski.email.service.impl;


import org.konradchrzanowski.email.domain.ConfirmationToken;
import org.konradchrzanowski.email.exception.ObjectNotFoundException;
import org.konradchrzanowski.email.mapper.ConfirmationTokenMapper;
import org.konradchrzanowski.email.repository.ConfirmationTokenRepository;
import org.konradchrzanowski.email.service.ConfirmationTokenService;
import org.konradchrzanowski.email.service.dto.ConfirmationTokenDTO;
import org.konradchrzanowski.utils.common.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    private static final int TOKEN_VALIDITY_TIME_IN_MINUTES = 30;
    private final Logger log = LoggerFactory.getLogger(ConfirmationTokenServiceImpl.class);

    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final ConfirmationTokenMapper confirmationTokenMapper;

    public ConfirmationTokenServiceImpl(ConfirmationTokenRepository confirmationTokenRepository,
                                        ConfirmationTokenMapper confirmationTokenMapper) {
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.confirmationTokenMapper = confirmationTokenMapper;
    }

    @Override
    public ConfirmationTokenDTO saveToken(String token, UserDTO userDTO) {
        log.debug("Request to save token to confirm user registration: {}", token);
        ConfirmationTokenDTO confirmationTokenDTO = new ConfirmationTokenDTO(null,
                token,
                userDTO.getId(),
                userDTO.getUsername(),
                userDTO.getEmail(),
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(TOKEN_VALIDITY_TIME_IN_MINUTES),
                null);
        ConfirmationToken confirmationToken = confirmationTokenMapper.toEntity(confirmationTokenDTO);
        ConfirmationToken saved = confirmationTokenRepository.save(confirmationToken);
        return confirmationTokenMapper.toDto(saved);
    }

    @Override
    public ConfirmationTokenDTO updateToken(ConfirmationTokenDTO confirmationTokenDTO) {
        log.debug("Request to update confirmed token: {}", confirmationTokenDTO.confirmationToken());
        ConfirmationToken confirmed = confirmationTokenRepository.save(
                confirmationTokenMapper.toEntity(new ConfirmationTokenDTO(
                        confirmationTokenDTO.id(),
                        confirmationTokenDTO.confirmationToken(),
                        confirmationTokenDTO.userId(),
                        confirmationTokenDTO.userName(),
                        confirmationTokenDTO.email(),
                        confirmationTokenDTO.createDate(),
                        confirmationTokenDTO.expireDate(),
                        LocalDateTime.now()
                )));
        return confirmationTokenMapper.toDto(confirmed);
    }

    @Override
    public ConfirmationTokenDTO getConfirmationToken(String token) {
        log.debug("Request to get confirmation token: {}", token);
        return confirmationTokenRepository.findByConfirmationToken(token).map(confirmationTokenMapper::toDto)
                .orElseThrow(() -> new ObjectNotFoundException("Token not found"));
    }

    @Override
    public void deleteConfirmationToken(Long id) {
        log.debug("Request to delete token after confirmation: {}", id);
        confirmationTokenRepository.deleteById(id);
    }

    @Override
    public String generateToken() {
        log.debug("Request to generate confirmation token");
        return UUID.randomUUID().toString();
    }
}
