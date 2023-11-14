package dev.flero.bismuth.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.flero.bismuth.modules.CameraTweaks;
import dev.flero.bismuth.modules.Rendering;
import dev.flero.bismuth.modules.Zoom;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

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
    public void setupCamera$cameraBobbing(GameRenderer instance, float tickDelta) {
        if (CameraTweaks.isEnabled && !CameraTweaks.disableCameraBobbing) this.bobView(tickDelta);
    }

    @Redirect(method = "renderHand", at = @At(value = "INVOKE", target = "net/minecraft/client/render/GameRenderer.bobView(F)V"))
    public void renderHand(GameRenderer instance, float tickDelta) {
        if (CameraTweaks.isEnabled && !CameraTweaks.disableHandBobbing) this.bobView(tickDelta);
    }

    @Inject(method = "bobView", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/GlStateManager;translate(FFF)V"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void bobView(float tickDelta, CallbackInfo ci, PlayerEntity entity, float g, float h, float i, float j) {
        if (CameraTweaks.isEnabled && CameraTweaks.minimalViewBobbing) {
            h /= 2;
            i /= 2;
            j /= 2;
            GlStateManager.translate(MathHelper.sin(h * (float) Math.PI) * i * 0.5f, -Math.abs(MathHelper.cos(h * (float) Math.PI) * i), 0.0f);
            GlStateManager.rotate(MathHelper.sin(h * (float) Math.PI) * i * 3.0f, 0.0f, 0.0f, 1.0f);
            GlStateManager.rotate(Math.abs(MathHelper.cos(h * (float) Math.PI - 0.2f) * i) * 5.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(j, 1.0f, 0.0f, 0.0f);
            ci.cancel();
        }
    }

    @Redirect(method = "transformCamera", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;rayTrace(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/hit/BlockHitResult;"))
    public BlockHitResult transformCamera$clipThroughPlants(ClientWorld instance, Vec3d from, Vec3d to) {
        boolean unused = false;
        boolean ignoreUncollidableBlocks = true;
        boolean fluidHandling = true;
        return instance.rayTrace(from, to, unused, ignoreUncollidableBlocks, fluidHandling);
    }

    @Inject(method = "bobViewWhenHurt", at = @At(value = "HEAD"), cancellable = true)
    private void bobViewWhenHurt(float tickDelta, CallbackInfo ci) {
        if (CameraTweaks.isEnabled && CameraTweaks.disableHurtCam) ci.cancel();
    }
}
