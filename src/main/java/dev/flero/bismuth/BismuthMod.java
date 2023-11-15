package dev.flero.bismuth;

import dev.flero.bismuth.commands.BismuthCommand;
import dev.flero.bismuth.config.ConfigManager;
import dev.flero.bismuth.modules.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.legacyfabric.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.legacyfabric.fabric.api.command.v2.CommandRegistrar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

public class BismuthMod implements ClientModInitializer {
    public static @Nullable ModContainer container;
    public static final Logger logger = LogManager.getLogger("Bismuth");
    public static final Instant startTime = Instant.now();

    @Override
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public void onInitializeClient() {
        container = FabricLoader.getInstance().getModContainer("bismuth").get();

        // Load configs
        logger.info("Loading config...");
        ConfigManager config = new ConfigManager();
        config.add(GameTitle.class);
        config.add(TitleCleaner.class);
        config.add(StartupLogo.class);
        config.add(AccountSwitcher.class);
        config.add(ScreenshotManager.class);
        config.add(Rendering.class);
        config.add(Zoom.class);
        config.add(ScrollableTooltips.class);
        config.add(CameraTweaks.class);
        config.add(DiscordRPC.class);

        DiscordRPC.updateClient();

        // Unload configs when the JVM closes (gracefully)
        // This will not run if JVM hard crashes, or if it receives a SIGKILL
        Runtime.getRuntime().addShutdownHook(new Thread(config::saveAll));
        Runtime.getRuntime().addShutdownHook(new Thread(DiscordRPC::shutdown));

        // Register commands
        logger.info("Registering commands...");
        CommandRegistrar.EVENT.register((manager, _dedicated) -> {
            logger.info("Registering /bismuth command...");
            manager.register(new BismuthCommand().createCallback(), "bismuth");
        });

        // Register keybinds
        logger.info("Registering keybinds...");
        KeyBindingHelper.registerKeyBinding(Zoom.keybinding);
        KeyBindingHelper.registerKeyBinding(Rendering.renderEntitiesKeybind);
    }
}
