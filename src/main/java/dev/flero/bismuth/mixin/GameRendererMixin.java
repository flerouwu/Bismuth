package dev.flero.bismuth.mixin;

import dev.flero.bismuth.modules.Rendering;
import dev.flero.bismuth.modules.Zoom;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow
    private MinecraftClient client;

    @Shadow
    protected abstract void bobView(float tickDelta);

    @Redirect(method = "updateLightmap", at = @At(value = "FIELD", target = "Lnet/minecraft/client/option/GameOptions;gamma:F"))
    public float updateLightmap(GameOptions instance) {
        if (Rendering.fullbrightEnabled) return Rendering.MAX_GAMMA;
        else return instance.gamma;
    }

    @Inject(method = "getFov", at = @At(value = "RETURN"), cancellable = true)
    public void getFov(float tickDelta, boolean changingFov, CallbackInfoReturnable<Float> cir) {
        if (Zoom.isEnabled) {
            double fov = cir.getReturnValue();
            double adjustment = Zoom.currentZoomLevel;
            if (Zoom.smoothZoom)
                adjustment = Zoom.currentSmoothLevel += (Zoom.currentZoomLevel - Zoom.currentSmoothLevel) * Zoom.smoothness;

            cir.setReturnValue((float) (fov * adjustment));


            if (Zoom.keybinding.isPressed() && !Zoom.isZooming) {
                // Zoom Start
                Zoom.originalSmoothCamera = client.options.smoothCameraEnabled;
                Zoom.currentZoomLevel = Zoom.zoomLevel;
                Zoom.isZooming = true;

                if (Zoom.cinematicCamera) client.options.smoothCameraEnabled = true;
            } else if (!Zoom.keybinding.isPressed() && Zoom.isZooming) {
                // Zoom End
                client.options.smoothCameraEnabled = Zoom.originalSmoothCamera;
                Zoom.isZooming = false;
                Zoom.currentZoomLevel = 1.0f;
            }
        }
    }

    @Redirect(method = "setupCamera", at = @At(value = "INVOKE", target = "net/minecraft/client/render/GameRenderer.bobView(F)V"))
    public void setupCamera(GameRenderer instance, float tickDelta) {
        if (Rendering.bobbingCameraEnabled) this.bobView(tickDelta);
    }

    @Redirect(method = "renderHand", at = @At(value = "INVOKE", target = "net/minecraft/client/render/GameRenderer.bobView(F)V"))
    public void renderHand(GameRenderer instance, float tickDelta) {
        if (Rendering.bobbingHandEnabled) this.bobView(tickDelta);
    }
}
