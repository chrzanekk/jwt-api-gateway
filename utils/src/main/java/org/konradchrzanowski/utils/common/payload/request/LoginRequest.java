package org.konradchrzanowski.utils.common.payload.request;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {

    private String username;

    @NotBlank
    private String password;

    private Boolean rememberMe;

}
