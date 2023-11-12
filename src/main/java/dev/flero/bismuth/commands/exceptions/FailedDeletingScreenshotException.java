package dev.flero.bismuth.commands.exceptions;

import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandException;
import net.minecraft.text.Text;

public class FailedDeletingScreenshotException extends CommandException {
    public FailedDeletingScreenshotException(Text message) {
        super(message);
    }
}
