package net.minecraft.client.gui.widget;

import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.network.ServerInfo;

import static net.minecraft.client.gui.DrawableHelper.fill;

/**
 * Legit just a class that extends {@link ServerEntry} so that we can access the constructor.
 * This feels so stupid but it works.
 */
public class BismuthServerEntry extends ServerEntry {
    public BismuthServerEntry(MultiplayerScreen multiplayerScreen, ServerInfo serverInfo) {
        super(multiplayerScreen, serverInfo);
    }

    public void render(int index, int x, int y, int rowWidth, int rowHeight, int mouseX, int mouseY, boolean hovered) {
        // Draw background
        fill(x - 2, y - 2, x + rowWidth + 2, y + rowHeight + 2, 0x80000000);

        super.render(index, x, y, rowWidth, rowHeight, mouseX, mouseY, hovered);
    }
}
