package dev.flero.bismuth.mixin;

import dev.flero.bismuth.modules.ScreenshotManager;
import net.minecraft.client.util.ScreenshotUtils;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Mixin(ScreenshotUtils.class)
public class ScreenshotUtilsMixin {
    @ModifyArgs(method = "saveScreenshot(Ljava/io/File;Ljava/lang/String;IILnet/minecraft/client/gl/Framebuffer;)Lnet/minecraft/text/Text;", at = @At(value = "INVOKE", target = "net/minecraft/text/TranslatableText.<init>(Ljava/lang/String;[Ljava/lang/Object;)V"))
    private static void saveScreenshot$TranslatableText(Args args) {
        if (!ScreenshotManager.isEnabled) return;

        // Set to custom Bismuth screenshot saved message.
        args.set(0, "bismuth.screenshot.saved");

        List<Object> params = Arrays.stream(args.<Object[]>get(1)).collect(Collectors.toList());

        Text copyText = new TranslatableText("bismuth.screenshot.copy");
        copyText.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/bismuth screenshots copy " + ((Text) params.get(0)).asUnformattedString()));
        params.add(copyText);

        Text openText = new TranslatableText("bismuth.screenshot.open");
        openText.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, "screenshots/" + ((Text) params.get(0)).asUnformattedString()));
        params.add(openText);

        Text deleteText = new TranslatableText("bismuth.screenshot.delete");
        deleteText.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/bismuth screenshots delete " + ((Text) params.get(0)).asUnformattedString()));
        params.add(deleteText);

        args.set(1, params.toArray());
    }
}
