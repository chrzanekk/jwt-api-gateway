package org.konradchrzanowski.utils.exception;

public class PasswordNotMatchException extends RuntimeException{

    public PasswordNotMatchException(String message) {
        super(message);
    }
}
