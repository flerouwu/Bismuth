package dev.flero.bismuth.modules;

import dev.flero.bismuth.config.ConfigHolder;
import dev.flero.bismuth.config.ConfigValue;

@ConfigHolder(name = "modules/camera_tweaks")
public class CameraTweaks {
    @ConfigValue
    public static boolean isEnabled = false;

    @ConfigValue
    public static boolean minimalViewBobbing = false;

    @ConfigValue
    public static boolean disableCameraBobbing = false;

    @ConfigValue
    public static boolean disableHandBobbing = false;

    @ConfigValue
    public static boolean disableHurtCam = false;
}
