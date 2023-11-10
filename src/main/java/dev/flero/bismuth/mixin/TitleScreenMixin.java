package dev.flero.bismuth.mixin;

import dev.flero.bismuth.BismuthMod;
import dev.flero.bismuth.modules.RealmsButton;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.api.metadata.Person;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.stream.Collectors;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
    @SuppressWarnings("SameReturnValue")
    @Redirect(method = "initWidgetsNormal", at = @At(value = "INVOKE", target = "java/util/List.add(Ljava/lang/Object;)Z"))
    private boolean initWidgetsNormal(List<ButtonWidget> list, Object obj) {
        ButtonWidget button = (ButtonWidget) obj;
        if (button.id == 14 && RealmsButton.isEnabled) {
            // Skip the realms button.
            return false;
        }

        list.add(button);
        return true;
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
    }
}
