package dev.flero.bismuth.mixin;

import dev.flero.bismuth.modules.RealmsButton;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
    @Redirect(method = "initWidgetsNormal", at = @At(value = "INVOKE", target = "java/util/List.add(Ljava/lang/Object;)Z"))
    private boolean initWidgetsNormal(List<ButtonWidget> list, Object obj) {
        ButtonWidget button = (ButtonWidget) obj;
        if (button.id == 14 && RealmsButton.isEnabled) {
            // Skip the realms button.
            return false;
        }

        list.add(button);
        return false;
    }
}
