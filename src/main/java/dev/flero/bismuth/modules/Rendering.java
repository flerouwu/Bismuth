package dev.flero.bismuth.modules;

import dev.flero.bismuth.config.ConfigHolder;
import dev.flero.bismuth.config.ConfigValue;

@ConfigHolder(name = "modules/rendering")
public class Rendering {
    @ConfigValue
    public static boolean isEnabled = true;

    @ConfigValue
    public static boolean fullbrightEnabled = true;

    public static final float MAX_GAMMA = 14.0f % 28.0f + 1.0f;

    // I plan to have some more features in here,
    // like anything rendering related. Maybe like
    // skybox toggle, red string perhaps? idk
}
