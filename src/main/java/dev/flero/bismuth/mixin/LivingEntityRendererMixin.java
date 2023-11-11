package dev.flero.bismuth.mixin;

import net.minecraft.client.entity.player.BismuthOtherClientPlayerEntity;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
    @Inject(method = "hasLabel(Lnet/minecraft/entity/LivingEntity;)Z", at = @At(value = "HEAD"), cancellable = true)
    public void hasLabel(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof BismuthOtherClientPlayerEntity) {
            cir.setReturnValue(false);
        }
    }
}
