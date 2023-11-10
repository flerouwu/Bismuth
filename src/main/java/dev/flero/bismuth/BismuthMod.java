package dev.flero.bismuth;

import dev.flero.bismuth.config.ConfigManager;
import dev.flero.bismuth.modules.GameTitle;
import dev.flero.bismuth.modules.RealmsButton;
import dev.flero.bismuth.modules.StartupLogo;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
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
        ConfigManager.loadConfig(GameTitle.class);
        ConfigManager.loadConfig(RealmsButton.class);
        ConfigManager.loadConfig(StartupLogo.class);
    }
}
