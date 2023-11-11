package dev.flero.bismuth.ui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;

public class ButtonWidget extends DrawableHelper implements Widget {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private final String text;
    private final Runnable onClick;

    public ButtonWidget(String text, Runnable onClick) {
        this.text = text;
        this.onClick = onClick;
    }

    @Override
    public void render(int mouseX, int mouseY, int x, int y, int width, int height) {
        drawCenteredString(client.textRenderer, text, x + width / 2, y + (height - 8) / 2, 0xFFFFFFFF);

        if (isMouseOver(mouseX, mouseY, x, y, width, height)) {
            drawVerticalLine(x, y, y + height, 0xFFFFFFFF);
            drawVerticalLine(x + width - 1, y, y + height, 0xFFFFFFFF);
            drawHorizontalLine(x, x + width - 1, y, 0xFFFFFFFF);
            drawHorizontalLine(x, x + width - 1, y + height - 1, 0xFFFFFFFF);
        }
    }

    @Override
    public Size getMinimumSize() {
        return new Size(-1, 15);
    }

    /**
     * Checks whether the player's mouse is currently over the button.
     *
     * @param mouseX The mouse's X position
     * @param mouseY The mouse's Y position
     * @return Whether the mouse is over the button
     */
    public boolean isMouseOver(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }
}
