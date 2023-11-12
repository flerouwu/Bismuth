package dev.flero.bismuth.ui;

import dev.flero.bismuth.ui.layout.Justify;
import dev.flero.bismuth.ui.layout.Padding;
import dev.flero.bismuth.ui.layout.VerticalLayout;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;

public class AccountSwitcherWidget extends DrawableHelper implements Widget {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private final VerticalLayout layout = new VerticalLayout(Justify.SpaceAround, new Padding().all(5).between(5), 0x80000000);

    public AccountSwitcherWidget() {
        layout.add(new ButtonWidget(client.getSession().getUsername(), () -> {
            System.out.println("Clicked!");
        }));
        layout.add(new PaperDollWidget(true), true);

    }

    @Override
    public void render(int mouseX, int mouseY, int x, int y, int width, int height) {
        layout.render(mouseX, mouseY, x, y, width, height);
    }

    @Override
    public Size getMinimumSize() {
        return layout.getMinimumSize();
    }
}
