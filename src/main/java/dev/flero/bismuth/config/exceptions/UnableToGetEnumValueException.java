package dev.flero.bismuth.config.exceptions;

public class UnableToGetEnumValueException extends ConfigException {
    public UnableToGetEnumValueException(String message) {
        super(message);
    }

    public UnableToGetEnumValueException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
