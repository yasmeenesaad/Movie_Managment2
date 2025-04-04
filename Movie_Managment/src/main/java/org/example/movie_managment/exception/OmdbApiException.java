package org.example.movie_managment.exception;

public class OmdbApiException extends RuntimeException {
    public OmdbApiException(String message) {
        super(message);
    }

    public OmdbApiException(String message, Throwable cause) {
        super(message, cause);
    }
}