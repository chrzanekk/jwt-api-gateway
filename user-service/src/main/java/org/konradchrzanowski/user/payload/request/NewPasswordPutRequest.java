package org.konradchrzanowski.user.payload.request;

public record NewPasswordPutRequest(
        String password,
        String confirmPassword,
        String token) {
}
