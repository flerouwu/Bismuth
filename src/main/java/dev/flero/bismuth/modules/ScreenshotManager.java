package dev.flero.bismuth.modules;

import dev.flero.bismuth.config.ConfigHolder;
import dev.flero.bismuth.config.ConfigValue;

@ConfigHolder(name = "modules/screenshots")
public class ScreenshotManager {
    @ConfigValue
    public static boolean isEnabled = true;

    @ConfigValue
    public static boolean deleteConfirmation = true;

    @ConfigValue
    public static int confirmTimeoutSeconds = 5;
}
