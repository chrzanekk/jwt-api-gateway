package org.konradchrzanowski.utils.exception;

public class EmailSendFailException extends RuntimeException {

    public EmailSendFailException(String message) {
        super(message);
    }

}
