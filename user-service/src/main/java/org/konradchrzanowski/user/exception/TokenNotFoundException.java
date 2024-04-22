package org.konradchrzanowski.user.exception;

public class TokenNotFoundException extends RuntimeException {

    public TokenNotFoundException(String message) {
        super(message);
    }
}
