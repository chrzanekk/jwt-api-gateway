package org.konradchrzanowski.user.service;

import org.konradchrzanowski.user.service.filter.UserFilter;
import org.konradchrzanowski.utils.common.dto.UserDTO;
import org.konradchrzanowski.utils.common.payload.request.RegisterRequest;
import org.konradchrzanowski.utils.common.payload.response.UserInfoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    UserDTO register(RegisterRequest request);

    String confirm(String token);

    UserDTO save(UserDTO userDTO);

    UserDTO update(UserDTO userDTO);

    List<UserDTO> findByFilter(UserFilter filter);

    Page<UserDTO> findByFilterAndPage(UserFilter filter, Pageable pageable);

    UserDTO findById(Long id);

    List<UserDTO> findAll();

    void delete(Long id);

    UserDTO getUser(String email);

    Boolean isUserExists(String userName);

    Boolean isEmailExists(String email);

    UserInfoResponse getUserWithAuthorities();
}
