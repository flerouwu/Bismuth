package dev.flero.bismuth.config.exceptions;

public class FieldWriteException extends RuntimeException {
    public FieldWriteException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
