package dev.flero.bismuth.commands;

import dev.flero.bismuth.BismuthMod;
import dev.flero.bismuth.chat.Component;
import dev.flero.bismuth.commands.exceptions.BismuthCommandException;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.api.metadata.Person;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandCallable;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandResult;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandContext;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.spec.CommandSpec;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;

import java.util.stream.Collectors;

public class VersionCommand implements Command {
    @Override
    public CommandResult execute(PermissibleCommandSource source, CommandContext context) throws CommandException {
        if (BismuthMod.container == null)
            throw new BismuthCommandException("bismuth.command.version.error.nullContainer");
        ModMetadata metadata = BismuthMod.container.getMetadata();

        // Version
        source.sendMessage(Component.translated("bismuth.command.version.message.version", metadata.getVersion().getFriendlyString()).toText());

        // Contributors
        String contribs = metadata.getContributors().stream().map(Person::getName).collect(Collectors.joining(", "));
        source.sendMessage(Component.translated("bismuth.command.version.message.contributors", contribs).toText());
        return CommandResult.success();
    }

    @Override
    public CommandCallable createCallback() {
        CommandSpec.Builder specification = CommandSpec.builder();
        specification.description(Component.translated("bismuth.command.version.description").toText());
        specification.executor(this::execute);
        return specification.build();
    }
}
