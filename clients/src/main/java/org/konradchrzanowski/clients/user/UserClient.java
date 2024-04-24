package org.konradchrzanowski.clients.user;

import org.konradchrzanowski.utils.common.dto.UserDTO;
import org.konradchrzanowski.utils.filters.UserFilter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "USER-SERVICE",
        url = "${clients.user.url}")
@RequestMapping(path = "/api/users")
public interface UserClient {

    @GetMapping(path = "/")
    ResponseEntity<List<UserDTO>> getUsersByFilter(UserFilter userFilter);

    @GetMapping(path = "/all")
    ResponseEntity<List<UserDTO>> getAllUsers();

    @GetMapping(path = "/page")
    ResponseEntity<List<UserDTO>> getUsersByFilterAndPage(UserFilter userFilter, Pageable pageable);

    @GetMapping(path = "/getById/{id}")
    ResponseEntity<UserDTO> getById(@PathVariable Long id);

    @DeleteMapping(path = "/delete/{id}")
    ResponseEntity<?> deleteUserById(@PathVariable Long id);

    @PutMapping(path = "/update")
    ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO);

    @PostMapping(path = "/save")
    ResponseEntity<UserDTO> saveUser(UserDTO userDTO);

    @GetMapping(path = "/user-exists")
    Boolean isUserExists(String userName);

    @GetMapping(path = "/email-exists")
    Boolean isEmailExists(String email);

    @GetMapping(path = "/get-user-by-email")
    ResponseEntity<UserDTO> getUserByEmail(String email);

    @GetMapping(path = "/get-user-by-userName")
    ResponseEntity<UserDTO> getUserByUserName(String userName);
}
