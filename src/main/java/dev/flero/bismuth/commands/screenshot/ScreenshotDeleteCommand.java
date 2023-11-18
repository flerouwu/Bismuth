package dev.flero.bismuth.commands.screenshot;

import dev.flero.bismuth.chat.Component;
import dev.flero.bismuth.commands.Command;
import dev.flero.bismuth.commands.ScreenshotCommand;
import dev.flero.bismuth.commands.exceptions.FailedDeletingScreenshotException;
import dev.flero.bismuth.modules.ScreenshotManager;
import net.legacyfabric.fabric.api.command.v2.StringType;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandCallable;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandResult;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandContext;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.GenericArguments;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.spec.CommandSpec;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ScreenshotDeleteCommand implements Command {
    private static final Set<String> awaitingConfirmation = new HashSet<>();

    @Override
    public CommandResult execute(PermissibleCommandSource source, CommandContext context) throws CommandException {
        String fileName = context.getAll("file").stream().map(value -> (String) value).collect(Collectors.joining(" "));
        File screenshot = ScreenshotCommand.getScreenshot(fileName);

        if (ScreenshotManager.Config.INSTANCE.getDeleteConfirmation() && !awaitingConfirmation.contains(screenshot.getAbsolutePath())) {
            awaitingConfirmation.add(screenshot.getAbsolutePath());
            new Thread(() -> {
                try {
                    Thread.sleep(ScreenshotManager.Config.INSTANCE.getConfirmTimeoutSeconds());
                } catch (InterruptedException ignored) {
                }

                awaitingConfirmation.remove(screenshot.getAbsolutePath());
            }).start();

            source.sendMessage(Component.translated(
                    "bismuth.command.screenshot.delete.message.confirm",
                    fileName,
                    ScreenshotManager.Config.INSTANCE.getConfirmTimeoutSeconds()
            ).toText());
        } else {
            if (!screenshot.delete()) {
                throw new FailedDeletingScreenshotException("screenshot.delete() returned false for file %s", fileName);
            }

            awaitingConfirmation.remove(screenshot.getAbsolutePath());
            source.sendMessage(Component.translated("bismuth.command.screenshot.delete.message.success", fileName).toText());
        }

        return CommandResult.success();
    }

    @Override
    public CommandCallable createCallback() {
        CommandSpec.Builder specification = CommandSpec.builder();
        specification.description(Component.translated("bismuth.command.screenshot.delete.description").toText());
        specification.arguments(GenericArguments.string(Component.text("file").toText(), StringType.GREEDY_PHRASE));
        specification.executor(this::execute);
        return specification.build();
    }
}
