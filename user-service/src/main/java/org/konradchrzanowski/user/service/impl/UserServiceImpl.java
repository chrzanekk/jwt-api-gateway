package org.konradchrzanowski.user.service.impl;

import jakarta.transaction.Transactional;
import org.konradchrzanowski.clients.token.TokenClient;
import org.konradchrzanowski.user.domain.User;
import org.konradchrzanowski.user.mapper.UserMapper;
import org.konradchrzanowski.user.repository.UserRepository;
import org.konradchrzanowski.user.service.RoleService;
import org.konradchrzanowski.user.service.UserService;
import org.konradchrzanowski.user.service.filter.UserSpecification;
import org.konradchrzanowski.utils.common.dto.ConfirmationTokenDTO;
import org.konradchrzanowski.utils.common.dto.RoleDTO;
import org.konradchrzanowski.utils.common.dto.UserDTO;
import org.konradchrzanowski.utils.common.enumeration.ERole;
import org.konradchrzanowski.utils.common.payload.request.RegisterRequest;
import org.konradchrzanowski.utils.email.EmailUtil;
import org.konradchrzanowski.utils.filters.UserFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);


    private final static String USER_NOT_FOUND = "user with email %s not found";

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final RoleService roleService;

    private final TokenClient tokenClient;

    private final PasswordEncoder encoder;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, RoleService roleService, TokenClient tokenClient, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleService = roleService;
        this.tokenClient = tokenClient;
        this.encoder = encoder;
    }


    @Override
    public UserDTO register(RegisterRequest request) {
        log.debug("Request to register new user: {}", request);
        EmailUtil.validateEmail(request.getEmail());
        Set<String> stringRoles = request.getRole();
        Set<RoleDTO> roleDTOSet = new HashSet<>();

        if (stringRoles == null || stringRoles.isEmpty()) {
            roleDTOSet.add(roleService.findByName(ERole.ROLE_USER));
        } else {
            stringRoles.forEach(role -> {
                switch (role) {
                    case "admin" -> {
                        RoleDTO adminRole = roleService.findByName(ERole.ROLE_ADMIN);
                        roleDTOSet.add(adminRole);
                    }
                    case "mod" -> {
                        RoleDTO modeRole = roleService.findByName(ERole.ROLE_MODERATOR);
                        roleDTOSet.add(modeRole);
                    }
                    default -> {
                        RoleDTO userRole = roleService.findByName(ERole.ROLE_USER);
                        roleDTOSet.add(userRole);
                    }
                }
            });
        }
        UserDTO newUser = UserDTO.builder().username(request.getUsername()).email(request.getEmail())
                .roles(roleDTOSet).enabled(false).locked(false).password(encoder.encode(request.getPassword()))
                .build();

        return save(newUser);
    }

    @Override
    public String confirm(String token) {
        ConfirmationTokenDTO confirmationTokenDTO = tokenClient.getConfirmationTokenByToken(token).getBody();
        assert confirmationTokenDTO != null;
        if (confirmationTokenDTO.confirmDate() != null) {
            throw new IllegalStateException("Email already confirmed");
        }

        LocalDateTime expireDate = confirmationTokenDTO.expireDate();
        if (expireDate.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token expired.");
        }

        ConfirmationTokenDTO confirmedDTO = tokenClient.updateConfirmationToken(confirmationTokenDTO).getBody();
        assert confirmedDTO != null;
        UserDTO user = getUser(confirmedDTO.email());
        update(UserDTO.builder(user).enabled(true).locked(false).build());
        return "Confirmed at" + confirmedDTO.confirmDate().toString();
    }

    @Override
    public UserDTO save(UserDTO userDTO) {
        log.info("Saving new user {} to database", userDTO);
        return userMapper.toDto(userRepository.save(userMapper.toEntity(userDTO)));
    }

    @Override
    public UserDTO update(UserDTO userDTO) {
        log.info("Update user {} to database", userDTO);
        return userMapper.toDto(userRepository.save(userMapper.toEntity(userDTO)));
    }

    @Override
    public List<UserDTO> findByFilter(UserFilter filter) {
        log.debug("Find all users by filter: {}", filter);
        Specification<User> specification = UserSpecification.create(filter);
        return userMapper.toDto(userRepository.findAll(specification));
    }

    @Override
    public Page<UserDTO> findByFilterAndPage(UserFilter filter, Pageable pageable) {
        log.debug("Find all users by filter and page: {}", filter);
        Specification<User> specification = UserSpecification.create(filter);
        return userRepository.findAll(specification, pageable).map(userMapper::toDto);
    }

    @Override
    public UserDTO findById(Long id) {
        log.debug("Find user by id: {}", id);
        Optional<User> optionalUser = userRepository.findById(id);
        return userMapper.toDto(optionalUser.orElseThrow(() -> new UsernameNotFoundException("User not found")));
    }

    @Override
    public List<UserDTO> findAll() {
        log.info("Fetching all users. ");
        return userMapper.toDto(userRepository.findAll());
    }

    @Override
    public void delete(Long id) {
        log.debug("Delete user by id: {}", id);
        userRepository.deleteById(id);
    }

    @Override
    public UserDTO getUser(String email) {
        log.info("Fetching user {} ", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, email)));
        return userMapper.toDto(user);
    }

    @Override
    public UserDTO getUserByUserName(String userName) {
        log.debug("Fetch user {} ", userName);
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, userName)));
        //todo check if roles are mapped correctly to dto
        return userMapper.toDto(user);
    }

    @Override
    public Boolean isUserExists(String userName) {
        log.debug("Request to check if userName exists in DB: {}", userName);
        return userRepository.existsByUsername(userName);
    }

    @Override
    public Boolean isEmailExists(String email) {
        log.debug("Request to check if email exists in DB: {}", email);
        return userRepository.existsByEmail(email);
    }

}
