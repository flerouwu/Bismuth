package dev.flero.bismuth.modules;

import dev.flero.bismuth.config.ConfigHolder;
import dev.flero.bismuth.config.ConfigValue;

@ConfigHolder(name = "modules/scrollable_tooltips")
public class ScrollableTooltips {
    @ConfigValue
    public static boolean isEnabled = true;

    @ConfigValue(comment = "Allow scrolling horizontally while holdering shift.")
    public static boolean scrollHorizontal = true;

    @ConfigValue(comment = "The amount to scroll per scroll tick.")
    public static int scrollAmount = 2;

    public static int tooltipX = 0;
    public static int tooltipY = 0;
}
