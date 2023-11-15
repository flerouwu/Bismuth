package dev.flero.bismuth.mixin.screens;

import dev.flero.bismuth.chat.Component;
import dev.flero.bismuth.modules.DiscordRPC;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiplayerScreen.class)
public class MultiplayerScreenMixin {
    @Inject(method = "init", at = @At("HEAD"))
    public void init(CallbackInfo ci) {
        DiscordRPC.setInfo(
                Component.translated("bismuth.rpc.title_multiplayer.details"),
                Component.translated("bismuth.rpc.title_multiplayer.state")
        );
    }
}
