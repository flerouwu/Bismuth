package dev.flero.bismuth.modules;

import dev.flero.bismuth.config.ConfigHolder;
import dev.flero.bismuth.config.ConfigValue;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.input.Keyboard;

@ConfigHolder(name = "modules/rendering")
public class Rendering {
    @ConfigValue
    public static boolean fullbrightEnabled = true;

    @ConfigValue(comment = "Enables the camera bobbing effect.")
    public static boolean bobbingCameraEnabled = true;

    @ConfigValue(comment = "Enables the hand bobbing effect.")
    public static boolean bobbingHandEnabled = true;

    @ConfigValue
    public static boolean minimalViewBobbing = false;

    @ConfigValue
    public static boolean hideCrosshairInF3 = true;

    public static final KeyBinding renderEntitiesKeybind = new KeyBinding("bismuth.keybind.render_entities", Keyboard.KEY_B, "bismuth.keybind.category");
    @ConfigValue
    public static boolean renderEntities = true;

    public static final float MAX_GAMMA = 14.0f % 28.0f + 1.0f;

    // I plan to have some more features in here,
    // like anything rendering related. Maybe like
    // skybox toggle, red string perhaps? idk
}
