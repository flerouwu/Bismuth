package dev.flero.bismuth.mixin;

import dev.flero.bismuth.modules.Rendering;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Unique
    private static final float MAX_GAMMA = 14.0f % 28.0f + 1.0f;

    @Redirect(method = "updateLightmap", at = @At(value = "FIELD", target = "Lnet/minecraft/client/option/GameOptions;gamma:F"))
    public float fullbright(GameOptions instance) {
        if (Rendering.isEnabled && Rendering.fullbrightEnabled) return MAX_GAMMA;
        else return instance.gamma;
    }
}
