package dev.flero.bismuth.modules;

import dev.flero.bismuth.BismuthMod;
import dev.flero.bismuth.config.ConfigHolder;
import dev.flero.bismuth.config.ConfigValue;

import java.text.MessageFormat;

@ConfigHolder(name = "modules/game_title")
public class GameTitle {
    @ConfigValue
    public static boolean isEnabled = true;

    @ConfigValue
    public static String template = "Bismuth {0} - {1}";

    public static String getTitle(String original) {
        if (isEnabled && BismuthMod.container != null) {
            MessageFormat format = new MessageFormat(template);
            return format.format(new Object[]{BismuthMod.container.getMetadata().getVersion().getFriendlyString(), original});
        } else {
            return original;
        }
    }
}
