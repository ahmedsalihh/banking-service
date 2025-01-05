package org.salih.banking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class NoInstallmentFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    public NoInstallmentFoundException(String message) {
        super(message);
    }
}