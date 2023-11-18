package dev.flero.bismuth;

import dev.flero.bismuth.commands.BismuthCommand;
import dev.flero.bismuth.config.ConfigManager;
import dev.flero.bismuth.i18n.I18n;
import dev.flero.bismuth.i18n.I18nTag;
import dev.flero.bismuth.modules.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.legacyfabric.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.legacyfabric.fabric.api.command.v2.CommandRegistrar;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

public class BismuthMod implements ClientModInitializer {
    private static final MinecraftClient client = MinecraftClient.getInstance();

    public static BismuthMod instance;
    public static @Nullable ModContainer container;
    public static final Logger logger = LogManager.getLogger("Bismuth");
    public static final Instant startTime = Instant.now();

    public ConfigManager config;

    @Override
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public void onInitializeClient() {
        instance = this;
        container = FabricLoader.getInstance().getModContainer("bismuth").get();
    }

    public void initializeGame() {
        // Load language
        logger.info("Loading language...");
        try {
            I18nTag tag = I18nTag.Companion.byTag(client.getLanguageManager().getLanguage().getCode());
            if (tag == null) throw new IllegalArgumentException("Invalid language tag.");
            I18n.Companion.loadLanguage(tag);
        } catch (IllegalArgumentException exception) {
            logger.warn("Unable to load language, falling back to " + I18n.Companion.getFallback().getTag() + ".");
            logger.warn("Minecraft language: " + client.getLanguageManager().getLanguage().getCode());
            logger.warn("It's likely that we don't have translations for this language yet.");
            logger.warn("If you wish to contribute, please open an issue on GitHub.");
            I18n.instance = I18n.Companion.getFallback();
        }

        logger.info("Loaded language " + I18n.instance.getTag() + ".");

        // Load configs
        logger.info("Loading config...");
        config = new ConfigManager();
        config.add(GameTitle.class);
        config.add(TitleCleaner.class);
        config.add(StartupLogo.class);
        config.add(AccountSwitcher.class);
        config.add(Rendering.class);
        config.add(Zoom.class);
        config.add(ScrollableTooltips.class);
        config.add(CameraTweaks.class);
        config.add(DiscordRPC.class);

        // Kotlin stuff
        new ScreenshotManager(this);

        DiscordRPC.updateClient();

        // Disconnect from Discord when the JVM closes (gracefully)
        // This will not run if JVM hard crashes, or if it receives a SIGKILL
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
