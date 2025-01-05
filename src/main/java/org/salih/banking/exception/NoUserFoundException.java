package org.salih.banking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class NoUserFoundException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    public NoUserFoundException(String message) {
        super(message);
    }
}
