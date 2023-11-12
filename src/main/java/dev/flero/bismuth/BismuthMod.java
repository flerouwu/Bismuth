package dev.flero.bismuth;

import dev.flero.bismuth.commands.BismuthCommand;
import dev.flero.bismuth.config.ConfigManager;
import dev.flero.bismuth.modules.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.legacyfabric.fabric.api.command.v2.CommandRegistrar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

public class BismuthMod implements ClientModInitializer {
    public static @Nullable ModContainer container;
    public static final Logger logger = LogManager.getLogger("Bismuth");

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

        // Unload configs when the JVM closes (gracefully)
        // This will not run if JVM hard crashes, or if it receives a SIGKILL
        Runtime.getRuntime().addShutdownHook(new Thread(config::saveAll));

        // Register commands
        logger.info("Registering commands...");
        CommandRegistrar.EVENT.register((manager, _dedicated) -> {
            logger.info("Registering /bismuth command...");
            manager.register(new BismuthCommand().createCallback(), "bismuth");
        });
    }
}
