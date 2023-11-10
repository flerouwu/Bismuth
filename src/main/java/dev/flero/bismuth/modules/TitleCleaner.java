package dev.flero.bismuth.modules;

import dev.flero.bismuth.config.ConfigHolder;
import dev.flero.bismuth.config.ConfigValue;

@ConfigHolder(name = "modules/title_cleaner")
public class TitleCleaner {
    @ConfigValue
    public static boolean isEnabled = true;

    @ConfigValue
    public static boolean removeRealms = true;

    @ConfigValue
    public static boolean removeSplash = false;

    @ConfigValue
    public static boolean removeLanguage = false;
}
