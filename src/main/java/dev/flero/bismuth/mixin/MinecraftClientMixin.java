package dev.flero.bismuth.mixin;

import dev.flero.bismuth.modules.GameTitle;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Redirect(method = "setPixelFormat", at = @At(value = "INVOKE", target = "org/lwjgl/opengl/Display.setTitle(Ljava/lang/String;)V"))
    public void setTitle(String title) {
        Display.setTitle(GameTitle.getTitle(title));
    }
}
