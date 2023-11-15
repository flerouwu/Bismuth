package dev.flero.bismuth.mixin.screens;

import dev.flero.bismuth.chat.Component;
import dev.flero.bismuth.modules.DiscordRPC;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SelectWorldScreen.class)
public class SelectWorldScreenMixin {
    @Inject(method = "init", at = @At("HEAD"))
    private void init(CallbackInfo ci) {
        DiscordRPC.setInfo(
                Component.translated("bismuth.rpc.title_singleplayer.details"),
                Component.translated("bismuth.rpc.title_singleplayer.state")
        );
    }
}
