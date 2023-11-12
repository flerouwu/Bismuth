package dev.flero.bismuth.config.exceptions;

public class FieldConversionException extends RuntimeException {
    public FieldConversionException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
