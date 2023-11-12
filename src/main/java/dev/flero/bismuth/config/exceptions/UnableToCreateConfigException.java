package dev.flero.bismuth.config.exceptions;

public class UnableToCreateConfigException extends RuntimeException {
    public UnableToCreateConfigException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
