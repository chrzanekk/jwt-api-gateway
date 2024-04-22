package org.konradchrzanowski.user.controller;

import lombok.AllArgsConstructor;
import org.konradchrzanowski.user.entities.UserVO;
import org.konradchrzanowski.user.service.OldUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users")
@AllArgsConstructor
public class UserController {

    private final OldUserService oldUserService;

    @PostMapping
    public ResponseEntity<UserVO> save(@RequestBody UserVO userVO) {
        return ResponseEntity.ok(oldUserService.save(userVO));
    }

    @GetMapping("/secured")
    public ResponseEntity<String> securedEndpoint() {
        return ResponseEntity.ok("Hello, from secured endpoint!");
    }
}
