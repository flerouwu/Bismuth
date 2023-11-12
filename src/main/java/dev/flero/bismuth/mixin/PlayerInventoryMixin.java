package dev.flero.bismuth.mixin;

import dev.flero.bismuth.modules.Zoom;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {
    @Inject(method = "scrollInHotbar", at = @At("HEAD"), cancellable = true)
    public void scrollInHotbar(int amount, CallbackInfo ci) {
        if (Zoom.isEnabled && Zoom.scrollZoom && Zoom.isZooming) {
            // These are reversed as we multiply the final value.
            // See GameRendererMixin.java for more info.
            float newLevel;
            if (amount > 0) newLevel = Zoom.currentZoomLevel - Zoom.zoomLevelStep;
            else newLevel = Zoom.currentZoomLevel + Zoom.zoomLevelStep;

            if (newLevel > 1.0f) newLevel = 1.0f;
            else if (newLevel < Zoom.maxZoomLevel) newLevel = Zoom.maxZoomLevel;

            Zoom.currentZoomLevel = newLevel;
            ci.cancel();
        }
    }
}
