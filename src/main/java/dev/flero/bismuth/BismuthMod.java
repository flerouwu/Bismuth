package dev.flero.bismuth;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.jetbrains.annotations.Nullable;

public class BismuthMod implements ClientModInitializer {
    public static @Nullable ModContainer container;

    @Override
    public void onInitializeClient() {
        container = FabricLoader.getInstance().getModContainer("bismuth").get();
    }
}
