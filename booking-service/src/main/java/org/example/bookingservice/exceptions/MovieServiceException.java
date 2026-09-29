package org.example.bookingservice.exceptions;

public class MovieServiceException extends RuntimeException {

    public MovieServiceException(String message) {
        super(message);
    }

    public MovieServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
