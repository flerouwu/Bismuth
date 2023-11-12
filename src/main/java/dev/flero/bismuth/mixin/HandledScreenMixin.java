package dev.flero.bismuth.mixin;

import dev.flero.bismuth.modules.ScrollableTooltips;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.inventory.slot.Slot;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(HandledScreen.class)
public class HandledScreenMixin {
    @Shadow
    private Slot focusedSlot;
    @Unique
    private long bismuth$systemTime = MinecraftClient.getTime();
    @Unique
    private ItemStack bismuth$lastItemStack = null;

    @ModifyArgs(method = "render", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/screen/ingame/HandledScreen.renderTooltip(Lnet/minecraft/item/ItemStack;II)V"))
    protected void renderTooltip(Args args) {
        bismuth$lastItemStack = args.get(0);
        if (ScrollableTooltips.isEnabled) {
            args.set(2, (int) args.get(2) + ScrollableTooltips.tooltipY);
            if (ScrollableTooltips.scrollHorizontal) args.set(1, (int) args.get(1) + ScrollableTooltips.tooltipX);
        }

        if ((MinecraftClient.getTime() - bismuth$systemTime) < 200L) {
            int wheel = Mouse.getDWheel();
            Mouse.next();

            int scroll = 0;
            if (wheel > 1) scroll = ScrollableTooltips.scrollAmount;
            else if (wheel < -1) scroll = -ScrollableTooltips.scrollAmount;

            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
                ScrollableTooltips.tooltipX += scroll;
            else ScrollableTooltips.tooltipY += scroll;
        }
    }

    @Inject(method = "render", at = @At(value = "HEAD"))
    public void render(int mouseX, int mouseY, float tickDelta, CallbackInfo ci) {
        bismuth$systemTime = MinecraftClient.getTime();
        if (this.focusedSlot == null || bismuth$lastItemStack != this.focusedSlot.getStack() || !this.focusedSlot.hasStack()) {
            ScrollableTooltips.tooltipY = 0;
            ScrollableTooltips.tooltipX = 0;
            bismuth$lastItemStack = null;
        }
    }
}
