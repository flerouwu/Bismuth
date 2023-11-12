package dev.flero.bismuth.config.exceptions;

public class UnableToWriteConfigException extends ConfigException {
    public UnableToWriteConfigException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
