package dev.flero.bismuth.commands;

import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandCallable;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandResult;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandContext;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;

public interface Command {
    CommandResult execute(PermissibleCommandSource source, CommandContext context) throws CommandException;

    CommandCallable createCallback();
}
