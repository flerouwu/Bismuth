package dev.flero.bismuth.commands.screenshot;

import dev.flero.bismuth.commands.Command;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandCallable;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandResult;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandContext;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.GenericArguments;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.spec.CommandSpec;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ScreenshotListCommand implements Command {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static final int SCREENSHOTS_PER_PAGE = 10;

    @Override
    public CommandResult execute(PermissibleCommandSource source, CommandContext context) throws CommandException {
        File screenshots = new File(client.runDirectory, "screenshots");
        if (!screenshots.exists())
            throw new CommandException(new TranslatableText("bismuth.command.screenshot.error.noScreenshots"));

        int page = (int) context.getOne("page").orElse(1);
        if (page < 1)
            throw new CommandException(new TranslatableText("bismuth.command.screenshot.error.invalidPage", page));

        // Get all files in the screenshots directory
        List<File> files = new ArrayList<>();
        for (File file : Arrays.stream(Objects.requireNonNull(screenshots.listFiles()))
                .skip((long) (page - 1) * SCREENSHOTS_PER_PAGE)
                .limit(SCREENSHOTS_PER_PAGE)
                .toArray(File[]::new)) {
            if (file.isFile()) files.add(file);
        }

        // Sort by last modified
        files = files.stream().sorted((a, b) -> Long.compare(b.lastModified(), a.lastModified())).collect(Collectors.toList());

        // Throw an error if there are no screenshots
        if (files.isEmpty())
            throw new CommandException(new TranslatableText("bismuth.command.screenshot.error.noScreenshots"));

        // Display the list of screenshots
        int totalPages = (int) Math.ceil((double) Objects.requireNonNull(screenshots.listFiles()).length / SCREENSHOTS_PER_PAGE);
        Text message = new TranslatableText("bismuth.command.screenshot.list.message.header", page, totalPages, files.size());
        for (File file : files) {
            message.append("\n").append(new TranslatableText("bismuth.command.screenshot.list.message.entry", file.getName()));
        }

        // Send the message
        source.sendMessage(message);
        return CommandResult.success();
    }

    @Override
    public CommandCallable createCallback() {
        CommandSpec.Builder specification = CommandSpec.builder();
        specification.description(new TranslatableText("bismuth.command.screenshot.list.description"));
        specification.arguments(GenericArguments.integer(new LiteralText("page")));
        specification.executor(this::execute);
        return specification.build();
    }
}
