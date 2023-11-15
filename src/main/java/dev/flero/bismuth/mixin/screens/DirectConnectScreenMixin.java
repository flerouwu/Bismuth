package dev.flero.bismuth.mixin.screens;

import dev.flero.bismuth.chat.Component;
import dev.flero.bismuth.modules.DiscordRPC;
import net.minecraft.client.gui.screen.DirectConnectScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.BismuthServerEntry;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.network.ServerInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DirectConnectScreen.class)
public class DirectConnectScreenMixin {
    @Shadow
    private TextFieldWidget serverField;

    @Shadow
    @Final
    private Screen parent;
    @Unique
    private String lastIP = null;

    @Unique
    private BismuthServerEntry statusEntry = null;

    @Inject(method = "init", at = @At("HEAD"))
    public void init(CallbackInfo ci) {
        DiscordRPC.setInfo(
                Component.translated("bismuth.rpc.title_direct_connect.details"),
                Component.translated("bismuth.rpc.title_direct_connect.state")
        );
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/screen/Screen.render(IIF)V"))
    public void render(int mouseX, int mouseY, float tickDelta, CallbackInfo ci) {
        if (statusEntry == null) this.initStatus();

        String ip = serverField.getText();
        if (!ip.equals(lastIP)) {
            lastIP = ip;
            this.initStatus();
        }

        statusEntry.render(0, ((Screen) (Object) this).width / 2 - 110 - 42, 42, 220 + 85, 32, mouseX, mouseY, false);
    }

    @Unique
    private void initStatus() {
        ServerInfo info = new ServerInfo(lastIP, lastIP, false);
        statusEntry = new BismuthServerEntry((MultiplayerScreen) parent, info);
    }
}
