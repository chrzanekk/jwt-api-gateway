package org.konradchrzanowski.utils.common.payload.request;

public record NewPasswordPutRequest(
        String password,
        String confirmPassword,
        String token) {
}
