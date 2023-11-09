package dev.flero.bismuth.modules;

public class GameTitle {
    public static boolean isEnabled = true;

    public static String getTitle(String original) {
        if (isEnabled) {
            return "Bismuth by Flero | " + original;
        } else {
            return original;
        }
    }
}
