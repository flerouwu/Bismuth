package dev.flero.bismuth.modules;

import dev.flero.bismuth.BismuthMod;
import dev.flero.bismuth.config.ConfigHolder;
import dev.flero.bismuth.config.ConfigValue;

@ConfigHolder(name = "modules/game_title")
public class GameTitle {
    @ConfigValue
    public static boolean isEnabled = true;

    public static String getTitle(String original) {
        if (isEnabled) {
            return String.format("Bismuth %s - %s", BismuthMod.container.getMetadata().getVersion().getFriendlyString(), original);
        } else {
            return original;
        }
    }
}
