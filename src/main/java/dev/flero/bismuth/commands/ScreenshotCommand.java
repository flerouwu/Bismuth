package dev.flero.bismuth.commands;

import dev.flero.bismuth.commands.screenshot.ScreenshotCopyCommand;
import dev.flero.bismuth.commands.screenshot.ScreenshotDeleteCommand;
import dev.flero.bismuth.commands.screenshot.ScreenshotListCommand;
import dev.flero.bismuth.commands.screenshot.ScreenshotOpenCommand;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandCallable;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandResult;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandContext;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.spec.CommandSpec;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.TranslatableText;

import java.io.File;

public class ScreenshotCommand implements Command {
    private static final MinecraftClient client = MinecraftClient.getInstance();

    public static File getScreenshot(String fileName) throws CommandException {
        fileName = "screenshots" + File.separator + fileName;
        File screenshot = new File(client.runDirectory, fileName);
        if (!screenshot.exists())
            throw new CommandException(new TranslatableText("bismuth.command.screenshot.error.notFound", fileName));

        return screenshot;
    }

    @Override
    public CommandResult execute(PermissibleCommandSource source, CommandContext context) throws CommandException {
        throw new CommandException(new TranslatableText("bismuth.command.screenshot.usage"));
    }

    @Override
    public CommandCallable createCallback() {
        CommandSpec.Builder specification = CommandSpec.builder();
        specification.description(new TranslatableText("bismuth.command.screenshot.description"));
        specification.child(new ScreenshotOpenCommand().createCallback(), "open");
        specification.child(new ScreenshotListCommand().createCallback(), "list");
        specification.child(new ScreenshotCopyCommand().createCallback(), "copy");
        specification.child(new ScreenshotDeleteCommand().createCallback(), "delete", "remove");
        return specification.build();
    }
}
