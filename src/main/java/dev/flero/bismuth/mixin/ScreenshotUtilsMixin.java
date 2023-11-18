package dev.flero.bismuth.mixin;

import dev.flero.bismuth.modules.ScreenshotManager;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.util.ScreenshotUtils;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.util.Objects;

@Mixin(ScreenshotUtils.class)
public class ScreenshotUtilsMixin {
    @Inject(method = "saveScreenshot(Ljava/io/File;Ljava/lang/String;IILnet/minecraft/client/gl/Framebuffer;)Lnet/minecraft/text/Text;", at = @At(value = "RETURN"), cancellable = true)
    private static void saveScreenshot$TranslatableText(File parent, String name, int textureWidth, int textureHeight, Framebuffer buffer, CallbackInfoReturnable<Text> cir) {
        if (!ScreenshotManager.Config.INSTANCE.isEnabled()) return;

        TranslatableText original = (TranslatableText) cir.getReturnValue();
        if (Objects.equals(original.getKey(), "screenshot.success")) {
            // Return our custom screenshot message if the original one was successful
            cir.setReturnValue(new LiteralText(ScreenshotManager.Translations.INSTANCE.getFormattedMessage()));
        }
    }
}
