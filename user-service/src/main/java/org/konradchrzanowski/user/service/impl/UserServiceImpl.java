package org.konradchrzanowski.user.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.konradchrzanowski.user.domain.User;
import org.konradchrzanowski.user.mapper.UserMapper;
import org.konradchrzanowski.user.repository.RoleRepository;
import org.konradchrzanowski.user.repository.UserRepository;
import org.konradchrzanowski.user.service.RoleService;
import org.konradchrzanowski.user.service.UserService;
import org.konradchrzanowski.user.service.filter.UserFilter;
import org.konradchrzanowski.user.service.filter.UserSpecification;
import org.konradchrzanowski.utils.common.dto.ConfirmationTokenDTO;
import org.konradchrzanowski.utils.common.dto.RoleDTO;
import org.konradchrzanowski.utils.common.dto.UserDTO;
import org.konradchrzanowski.utils.common.enumeration.ERole;
import org.konradchrzanowski.utils.common.payload.request.RegisterRequest;
import org.konradchrzanowski.utils.common.payload.response.UserInfoResponse;
import org.konradchrzanowski.utils.email.EmailUtil;
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

@AllArgsConstructor
@Log4j2
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final static String USER_NOT_FOUND = "user with email %s not found";

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UserMapper userMapper;

    private final RoleService roleService;
    //todo inject confirmationTokenService client from auth-service
//    private final ConfirmationTokenService confirmationTokenService;

    private final PasswordEncoder encoder;


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
        ConfirmationTokenDTO confirmationTokenDTO = confirmationTokenService.getConfirmationToken(token);
        if (confirmationTokenDTO.confirmDate() != null) {
            throw new IllegalStateException("Email already confirmed");
        }

        LocalDateTime expireDate = confirmationTokenDTO.expireDate();
        if (expireDate.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token expired.");
        }

        ConfirmationTokenDTO confirmedDTO = confirmationTokenService.updateToken(confirmationTokenDTO);
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
    public Boolean isUserExists(String userName) {
        log.debug("Request to check if userName exists in DB: {}", userName);
        return userRepository.existsByUsername(userName);
    }

    @Override
    public Boolean isEmailExists(String email) {
        log.debug("Request to check if email exists in DB: {}", email);
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserInfoResponse getUserWithAuthorities() {
        String currentLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new UsernameNotFoundException("User not found"));
        User currentUser = userRepository.findByUsername(currentLogin).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<ERole> currentRoles = currentUser.getRoles().stream().map(Role::getName).toList();
        return new UserInfoResponse(
                currentUser.getId(),
                currentUser.getUsername(),
                currentUser.getEmail(),
                currentRoles);
    }

}
