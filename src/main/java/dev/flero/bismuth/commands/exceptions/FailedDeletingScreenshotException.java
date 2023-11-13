package dev.flero.bismuth.commands.exceptions;

import dev.flero.bismuth.chat.Component;

public class FailedDeletingScreenshotException extends BismuthCommandException {
    public FailedDeletingScreenshotException(String text, Object... args) {
        super(Component.text(text, args));
    }
}
