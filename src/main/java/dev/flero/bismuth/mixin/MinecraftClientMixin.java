package dev.flero.bismuth.mixin;

import dev.flero.bismuth.chat.Component;
import dev.flero.bismuth.modules.GameTitle;
import dev.flero.bismuth.modules.Rendering;
import dev.flero.bismuth.modules.StartupLogo;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.resource.DefaultResourcePack;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.io.InputStream;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow
    public ClientPlayerEntity player;

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
}
