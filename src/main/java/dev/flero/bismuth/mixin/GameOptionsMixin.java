package dev.flero.bismuth.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(GameOptions.class)
public class GameOptionsMixin {
    @Inject(method = "<init>(Lnet/minecraft/client/MinecraftClient;Ljava/io/File;)V", at = @At(value = "RETURN"))
    public void onInit(MinecraftClient minecraftClient, File file, CallbackInfo ci) {
        GameOptions.Option.RENDER_DISTANCE.setMaxValue(256.0f);
    }
}
