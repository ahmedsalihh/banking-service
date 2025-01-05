package org.salih.banking.exception;

import java.util.Date;

public class ErrorDetails {
    private Date timestamp;
    private String message;
    private int errorCode;
    private String details;

    public ErrorDetails(Date timestamp, String message, int errorCode, String details) {
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.errorCode = errorCode;
        this.details = details;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
