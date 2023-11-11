package dev.flero.bismuth.ui.layout;

import dev.flero.bismuth.ui.Size;
import dev.flero.bismuth.ui.Widget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VerticalLayout extends DrawableHelper implements Widget {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static final Logger LOGGER = LogManager.getLogger("Bismuth/VerticalLayout");
    private static final boolean DEBUG = false;
    public final @Nullable Justify justify;
    public final @Nullable Padding padding;
    public final @Nullable Integer backgroundColour;
    public List<VerticalLayout.Entry> children = new ArrayList<>();

    public VerticalLayout(@Nullable Justify justify, @Nullable Padding padding, @Nullable Integer backgroundColour) {
        this.justify = justify;
        this.padding = padding;
        this.backgroundColour = backgroundColour;
    }

    public VerticalLayout add(Widget child) {
        return add(child, false);
    }

    public VerticalLayout add(Widget child, boolean fillSpace) {
        children.add(new Entry(child, fillSpace));
        return this;
    }

    @Override
    public void render(int mouseX, int mouseY, int x, int y, int width, int height) {
        if (backgroundColour != null) fill(x, y, x + width, y + height, backgroundColour);

        int totalSize = children.stream().map((entry) -> entry.widget.getMinimumSize().height).reduce(0, Integer::sum);
        int extraSpace = height - totalSize;
        if (padding != null) {
            extraSpace -= padding.top;
            extraSpace -= padding.bottom;
            extraSpace -= padding.between * (children.size() - 1);
        }

        int yOffset = 0;

        for (Entry entry : children) {
            Widget widget = entry.widget;
            Size widgetSize = widget.getMinimumSize();

            // Throw errors on invalid sizes
            if (widgetSize.height > height || yOffset > height)
                LOGGER.fatal("Child widget is too big for parent (Height)!", new IllegalStateException());
            if (widgetSize.width > width)
                LOGGER.warn("Child widget is too big for parent (Width)!", new IllegalStateException());

            // Render
            int renderX = x;
            int renderY = y + yOffset;
            int renderWidth = width;
            int renderHeight = widgetSize.height;

            // Padding
            if (padding != null) {
                renderX += padding.left;
                renderY += padding.top;
                renderWidth -= padding.left + padding.right;
                // height is already managed by extraSpace

                boolean isLast = children.indexOf(entry) == children.size() - 1;
                if (!isLast) {
                    yOffset += padding.between;
                }
            }

            if (entry.fillSpace) renderHeight += extraSpace;

            widget.render(mouseX, mouseY, renderX, renderY, renderWidth, renderHeight);
            if (DEBUG) {
                drawVerticalLine(renderX, renderY, renderY + renderHeight - 1, 0xFFFF0000);
                drawVerticalLine(renderX + renderWidth - 1, renderY, renderY + renderHeight - 1, 0xFFFF0000);

                drawHorizontalLine(renderX, renderX + renderWidth - 1, renderY, 0xFFFF0000);
                drawHorizontalLine(renderX, renderX + renderWidth - 1, renderY + renderHeight - 1, 0xFFFF0000);

                // Class.getSimpleName(); #FF0000 (red) at 0.5 opacity
                drawCenteredString(client.textRenderer, entry.getClass().getSimpleName(), renderX + renderWidth / 2, renderY + renderHeight / 2, 0x80FF0000);
            }

            yOffset += renderHeight;
        }
    }

    @Override
    public Size getMinimumSize() {
        int width = 0;
        int height = 0;

        for (Entry entry : children) {
            Size size = entry.widget.getMinimumSize();
            if (width > size.width) width = size.width;
            height += size.height;
        }

        return new Size(width, height);
    }

    public static class Entry {
        public final Widget widget;
        public final boolean fillSpace;

        protected Entry(Widget widget, boolean fillSpace) {
            this.widget = widget;
            this.fillSpace = fillSpace;
        }
    }
}
