package dev.flero.bismuth.modules;

import dev.flero.bismuth.BismuthMod;

public class GameTitle {
    public static boolean isEnabled = true;

    public static String getTitle(String original) {
        if (isEnabled) {
            return String.format("Bismuth %s - %s", BismuthMod.version, original);
        } else {
            return original;
        }
    }
}
