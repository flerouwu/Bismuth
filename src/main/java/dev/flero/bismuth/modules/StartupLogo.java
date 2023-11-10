package dev.flero.bismuth.modules;

import dev.flero.bismuth.config.ConfigHolder;
import dev.flero.bismuth.config.ConfigValue;
import net.minecraft.util.Identifier;

@ConfigHolder(name = "modules/startup_logo")
public class StartupLogo {
    @ConfigValue
    public static boolean isEnabled = true;

    @ConfigValue
    public static Identifier logo = new Identifier("bismuth", "textures/gui/title/bismuth.png");
}
