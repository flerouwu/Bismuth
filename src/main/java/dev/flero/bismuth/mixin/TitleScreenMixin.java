package dev.flero.bismuth.mixin;

import dev.flero.bismuth.BismuthMod;
import dev.flero.bismuth.modules.AccountSwitcher;
import dev.flero.bismuth.modules.TitleCleaner;
import dev.flero.bismuth.ui.AccountSwitcherWidget;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.api.metadata.Person;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.stream.Collectors;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
    @Unique
    private static final AccountSwitcherWidget accountSwitcher = new AccountSwitcherWidget();

    @SuppressWarnings("SameReturnValue")
    @Redirect(method = "initWidgetsNormal", at = @At(value = "INVOKE", target = "java/util/List.add(Ljava/lang/Object;)Z"))
    private boolean initWidgetsNormal(List<ButtonWidget> list, Object obj) {
        ButtonWidget button = (ButtonWidget) obj;
        if (TitleCleaner.isEnabled) {
            if (button.id == 14 && TitleCleaner.removeRealms) return false;
        }

        list.add(button);
        return true;
    }

    @Redirect(method = "init", at = @At(value = "INVOKE", target = "java/util/List.add(Ljava/lang/Object;)Z"))
    public boolean init(List<ButtonWidget> list, Object obj) {
        ButtonWidget button = (ButtonWidget) obj;
        if (TitleCleaner.isEnabled) {
            if (button.id == 5 && TitleCleaner.removeLanguage) return false;
        }

        list.add(button);
        return true;
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/screen/TitleScreen.drawCenteredString(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"))
    public void drawCenteredString(TitleScreen screen, net.minecraft.client.font.TextRenderer textRenderer, String text, int x, int y, int color) {
        if (TitleCleaner.isEnabled && TitleCleaner.removeSplash) return;
        screen.drawCenteredString(textRenderer, text, x, y, color);
    }

    @Inject(method = "render", at = @At(value = "TAIL"))
    public void render(int mouseX, int mouseY, float tickDelta, CallbackInfo ci) {
        TitleScreen screen = (TitleScreen) (Object) this;
        if (BismuthMod.container == null) return;

        ModMetadata metadata = BismuthMod.container.getMetadata();
        screen.drawWithShadow(screen.textRenderer, "Bismuth v" + metadata.getVersion().getFriendlyString(), 2, 2, 0xFFFFFF);

        StringBuilder contributors = new StringBuilder();
        contributors.append("Contributors: ");
        for (String contributor : metadata.getContributors().stream().map(Person::getName).collect(Collectors.toList())) {
            contributors.append(contributor);
            contributors.append(", ");
        }

        contributors.delete(contributors.length() - 2, contributors.length());
        screen.drawWithShadow(screen.textRenderer, contributors.toString(), 2, 12, 0xFFFFFF);

        // Draw Player Widget
        if (AccountSwitcher.isEnabled) {
            int height = screen.height / 4 + 48;
            int width = screen.width / 2;

            if (AccountSwitcher.widgetSide == AccountSwitcher.WidgetSide.LEFT) {
                width -= 64;
                width -= 36;
                width -= 64; // Width of the player widget
                width -= 5; // Padding

                // Account for language button
                if (!TitleCleaner.removeLanguage)
                    width -= 24;
            } else {
                width += 36;
                width += 64; // Width of the player widget
                width += 5; // Padding
            }

            accountSwitcher.render(mouseX, mouseY, width, height, 64, 105);
        }
    }
}
