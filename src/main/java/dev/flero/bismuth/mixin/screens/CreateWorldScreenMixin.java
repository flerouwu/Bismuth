package dev.flero.bismuth.mixin.screens;

import dev.flero.bismuth.modules.DiscordRPC;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreateWorldScreen.class)
public class CreateWorldScreenMixin {
    @Inject(method = "init", at = @At("HEAD"))
    public void init(CallbackInfo ci) {
        DiscordRPC.setInfo("bismuth.rpc.create_world.details", "bismuth.rpc.create_world.state");
    }
}
