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

        logger.info("Loading config...");
        ConfigManager config = new ConfigManager();
        config.loadConfig(GameTitle.class);
        config.loadConfig(TitleCleaner.class);
        config.loadConfig(StartupLogo.class);
        config.loadConfig(AccountSwitcher.class);
        config.loadConfig(ScreenshotManager.class);

        logger.info("Registering commands...");
        CommandRegistrar.EVENT.register((manager, _dedicated) -> {
            logger.info("Registering /bismuth command...");
            manager.register(new BismuthCommand().createCallback(), "bismuth");
        });
    }
}
