package org.konradchrzanowski.email.exception;

public class EmailSendFailException extends RuntimeException {

    public EmailSendFailException(String message) {
        super(message);
    }

}
