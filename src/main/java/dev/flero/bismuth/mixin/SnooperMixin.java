package dev.flero.bismuth.mixin;

import net.minecraft.util.snooper.Snooper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Timer;

@Mixin(Snooper.class)
public class SnooperMixin {
    @Shadow
    private boolean active;

    @Shadow
    @Final
    private Timer timer;

    @Inject(method = "setActive", at = @At(value = "HEAD"), cancellable = true)
    public void setActive(CallbackInfo ci) {
        System.out.println("Snooper#setActive(boolean bl) was called! Returning the method early...");
        active = false;
        timer.cancel();
        ci.cancel();
    }
}
