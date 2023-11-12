package dev.flero.bismuth.commands.screenshot;

import dev.flero.bismuth.commands.Command;
import dev.flero.bismuth.commands.ScreenshotCommand;
import net.legacyfabric.fabric.api.command.v2.StringType;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandCallable;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandResult;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandContext;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.GenericArguments;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.spec.CommandSpec;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

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
            source.sendMessage(new TranslatableText("bismuth.command.screenshot.open.message", fileName));
        } catch (IOException exception) {
            throw new CommandException(new TranslatableText("bismuth.command.screenshot.error.open", fileName));
        }
        return CommandResult.success();
    }

    @Override
    public CommandCallable createCallback() {
        CommandSpec.Builder specification = CommandSpec.builder();
        specification.description(new TranslatableText("bismuth.command.screenshot.open.description"));
        specification.arguments(GenericArguments.string(new LiteralText("file"), StringType.GREEDY_PHRASE));
        specification.executor(this::execute);
        return specification.build();
    }
}
