package dev.flero.bismuth.modules;

import dev.flero.bismuth.config.ConfigHolder;
import dev.flero.bismuth.config.ConfigValue;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.input.Keyboard;

@ConfigHolder(name = "modules/zoom")
public class Zoom {
    @ConfigValue
    public static boolean isEnabled = true;
    public static boolean isZooming = false;

    @ConfigValue(comment = "Smooths the FOV adjustment when zooming.")
    public static boolean smoothZoom = true;

    @ConfigValue(comment = "Smooths the camera movement while zoomed.")
    public static boolean cinematicCamera = true;
    public static boolean originalSmoothCamera = false;

    @ConfigValue(comment = "Amount of zoom we should add.")
    public static float zoomLevel = 0.25f;
    public static float currentZoomLevel = 1.0f;
    public static float currentSmoothLevel = 1.0f;
    public static float smoothness = 0.025f;
    public static float zoomLevelStep = 0.02f;

    @ConfigValue(comment = "If enabled, scrolling with the mouse scroll wheel will adjust the level of zoom.")
    public static boolean scrollZoom = true;

    @ConfigValue(comment = "Maximum zoom level when using scroll zoom.")
    public static float maxZoomLevel = 0.01f;

    public static KeyBinding keybinding = new KeyBinding("bismuth.keybind.zoom", Keyboard.KEY_C, "bismuth.keybind.category");
}
