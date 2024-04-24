package org.konradchrzanowski.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.konradchrzanowski.user.service.UserService;
import org.konradchrzanowski.utils.common.dto.UserDTO;
import org.konradchrzanowski.utils.common.payload.response.UserInfoResponse;
import org.konradchrzanowski.utils.controller.PaginationUtil;
import org.konradchrzanowski.utils.filters.UserFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;


@RestController
@RequestMapping(value = "/api/users")
public class UserController {
    private final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/secured")
    public ResponseEntity<String> securedEndpoint() {
        return ResponseEntity.ok("Hello, from secured endpoint!");
    }


    @GetMapping(path = "/")
    public ResponseEntity<List<UserDTO>> getUsersByFilter(UserFilter userFilter) {
        log.debug("REST request to get all users by filter: {}", userFilter);
        List<UserDTO> result = userService.findByFilter(userFilter);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        log.debug("REST request to get all users.");
        List<UserDTO> result = userService.findAll();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(path = "/page")
    public ResponseEntity<List<UserDTO>> getUsersByFilterAndPage(UserFilter userFilter, Pageable pageable) {
        log.debug("REST request to get all users by filter and page: {},{}", userFilter, pageable);
        Page<UserDTO> result = userService.findByFilterAndPage(userFilter, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequestUri(), result);
        return ResponseEntity.ok().headers(headers).body(result.getContent());
    }

    @GetMapping(path = "/getById/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id) {
        log.debug("REST request to get user by id: {}", id);
        UserDTO userDTO = userService.findById(id);
        return ResponseEntity.ok().body(userDTO);
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        log.debug("REST request to delete user by id: {}", id);
        userService.delete(id);
        return ResponseEntity.ok().build();
    }



    @PutMapping(path = "/update")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO) {
        log.debug("REST request to update user: {}", userDTO);
        UserDTO result = userService.update(userDTO);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping(path = "/save")
    public ResponseEntity<UserDTO> saveUser(UserDTO userDTO) {
        log.debug("REST request to save user: {}", userDTO);
        UserDTO result = userService.save(userDTO);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(path = "/user-exists")
    public Boolean isUserExists(String userName) {
        log.debug("Request to check if username exists in database: {}", userName);
        return userService.isUserExists(userName);
    }

    @GetMapping(path = "/email-exists")
    public Boolean isEmailExists(String email) {
        log.debug("Request to check if email exists in database: {}", email);
        return userService.isEmailExists(email);
    }

    @GetMapping(path = "/get-user-by-email")
    public ResponseEntity<UserDTO> getUserByEmail(String email) {
        log.debug("Request to get user by email: {}", email);
        UserDTO userDTO = userService.getUser(email);
        return ResponseEntity.ok().body(userDTO);
    }

    @GetMapping(path = "/get-user-by-userName")
    public ResponseEntity<UserDTO> getUserByUserName(String userName) {
        log.debug("Request to get user by userName: {}", userName);
        UserDTO userDTO = userService.getUserByUserName(userName);
        return ResponseEntity.ok().body(userDTO);
    }
}
