package dev.flero.bismuth.commands;

import dev.flero.bismuth.commands.exceptions.BismuthCommandException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandCallable;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandResult;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandContext;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.spec.CommandSpec;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;
import net.minecraft.text.TranslatableText;

public class BismuthCommand implements Command {
    @Override
    public CommandResult execute(PermissibleCommandSource source, CommandContext context) throws CommandException {
        throw new BismuthCommandException("bismuth.command.main.usage");
    }

    @Override
    public CommandCallable createCallback() {
        CommandSpec.Builder specification = CommandSpec.builder();
        specification.description(new TranslatableText("bismuth.command.main.description"));
        specification.child(new VersionCommand().createCallback(), "version");
        specification.child(new ScreenshotCommand().createCallback(), "screenshots", "screenshot");

        return specification.build();
    }
}
