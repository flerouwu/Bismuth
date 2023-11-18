package dev.flero.bismuth.mixin;

import dev.flero.bismuth.BismuthMod;
import dev.flero.bismuth.chat.Component;
import dev.flero.bismuth.modules.DiscordRPC;
import dev.flero.bismuth.modules.GameTitle;
import dev.flero.bismuth.modules.Rendering;
import dev.flero.bismuth.modules.StartupLogo;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.resource.DefaultResourcePack;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.IOException;
import java.io.InputStream;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow
    public ClientPlayerEntity player;

    @Shadow
    private IntegratedServer server;

    @Shadow
    private String serverAddress;

    @Shadow
    protected abstract void setGlErrorMessage(String message);

    @Inject(method = "initializeGame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;<init>(Lnet/minecraft/client/MinecraftClient;)V"))
    public void initializeGame(CallbackInfo ci) {
        setGlErrorMessage("Loading Bismuth");
        BismuthMod.instance.initializeGame();
        setGlErrorMessage("Post startup");
    }

    @Redirect(method = "setPixelFormat", at = @At(value = "INVOKE", target = "org/lwjgl/opengl/Display.setTitle(Ljava/lang/String;)V"))
    public void setTitle(String title) {
        Display.setTitle(GameTitle.getTitle(title));
    }

    @Redirect(method = "loadLogo", at = @At(value = "INVOKE", target = "net/minecraft/resource/DefaultResourcePack.open(Lnet/minecraft/util/Identifier;)Ljava/io/InputStream;"))
    public InputStream setLogo(DefaultResourcePack instance, Identifier id) throws IOException {
        if (!StartupLogo.isEnabled) {
            return instance.open(id);
        }

        id = StartupLogo.logo;
        return instance.open(id);
    }

    @Inject(method = "tick", at = @At(value = "TAIL"))
    public void tick(CallbackInfo ci) {
        if (Rendering.renderEntitiesKeybind.wasPressed()) {
            Rendering.renderEntities = !Rendering.renderEntities;
            if (Rendering.renderEntities)
                player.sendMessage(Component.text("Now rendering entities.").style(Formatting.GREEN).toText());
            else player.sendMessage(Component.text("No longer rendering entities.").style(Formatting.RED).toText());
        }
    }

    @Inject(method = "connect(Lnet/minecraft/client/world/ClientWorld;Ljava/lang/String;)V", at = @At(value = "TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void connect(ClientWorld world, String loadingMessage, CallbackInfo ci) {
        if (server == null) {
            DiscordRPC.setInfo(
                    Component.translated("bismuth.rpc.game_multiplayer.details"),
                    Component.translated("bismuth.rpc.game_multiplayer.state", serverAddress)
            );
        }
    }
}
