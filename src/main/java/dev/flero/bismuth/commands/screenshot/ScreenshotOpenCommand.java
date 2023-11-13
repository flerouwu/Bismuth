package dev.flero.bismuth.commands.screenshot;

import dev.flero.bismuth.chat.Component;
import dev.flero.bismuth.commands.Command;
import dev.flero.bismuth.commands.ScreenshotCommand;
import dev.flero.bismuth.commands.exceptions.BismuthCommandException;
import net.legacyfabric.fabric.api.command.v2.StringType;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandCallable;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandResult;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandContext;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.GenericArguments;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.spec.CommandSpec;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;

public class ScreenshotOpenCommand implements Command {
    @Override
    public CommandResult execute(PermissibleCommandSource source, CommandContext context) throws CommandException {
        String fileName = context.getAll("file").stream().map(value -> (String) value).collect(Collectors.joining(" "));
        File screenshot = ScreenshotCommand.getScreenshot(fileName);

        try {
            Desktop.getDesktop().open(screenshot);
            source.sendMessage(Component.translated("bismuth.command.screenshot.open.message", fileName).toText());
        } catch (IOException exception) {
            throw new BismuthCommandException("bismuth.command.screenshot.error.open", fileName);
        }
        return CommandResult.success();
    }

    @Override
    public CommandCallable createCallback() {
        CommandSpec.Builder specification = CommandSpec.builder();
        specification.description(Component.translated("bismuth.command.screenshot.open.description").toText());
        specification.arguments(GenericArguments.string(Component.text("file").toText(), StringType.GREEDY_PHRASE));
        specification.executor(this::execute);
        return specification.build();
    }
}
