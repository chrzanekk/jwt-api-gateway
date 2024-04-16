package org.konradchrzanowski.auth.controllers;

import lombok.AllArgsConstructor;
import org.konradchrzanowski.auth.services.AuthService;
import org.konradchrzanowski.auth.entities.AuthRequest;
import org.konradchrzanowski.auth.entities.AuthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/register")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
}
