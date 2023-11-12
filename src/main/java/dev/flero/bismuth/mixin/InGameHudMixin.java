package dev.flero.bismuth.mixin;

import dev.flero.bismuth.modules.Rendering;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "showCrosshair", at = @At("HEAD"), cancellable = true)
    private void showCrosshair(CallbackInfoReturnable<Boolean> cir) {
        if (Rendering.hideCrosshairInF3 && client.options.perspective > 0) cir.setReturnValue(false);
    }
}
