package dev.flero.bismuth.modules;

import dev.flero.bismuth.config.ConfigHolder;
import dev.flero.bismuth.config.ConfigValue;

/**
 * Removes the Realms button from the TitleScreen.
 */
@ConfigHolder(name = "modules/realms")
public class RealmsButton {
    @ConfigValue
    public static boolean isEnabled = true;
}
