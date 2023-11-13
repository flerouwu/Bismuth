package dev.flero.bismuth.commands.exceptions;

import dev.flero.bismuth.chat.Component;
import dev.flero.bismuth.chat.TranslatableComponent;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandException;

public class BismuthCommandException extends CommandException {
    public BismuthCommandException(Component component) {
        super(component.toText());
    }

    public BismuthCommandException(String key, Object... args) {
        super(new TranslatableComponent(key, args).toText());
    }
}
