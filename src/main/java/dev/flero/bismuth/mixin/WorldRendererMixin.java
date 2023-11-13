package dev.flero.bismuth.mixin;

import dev.flero.bismuth.modules.Rendering;
import net.minecraft.client.render.CameraView;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Inject(method = "renderEntities", at = @At("HEAD"), cancellable = true)
    public void renderEntities(Entity entity, CameraView cameraView, float tickDelta, CallbackInfo ci) {
        if (!Rendering.renderEntities) ci.cancel();
    }
}
