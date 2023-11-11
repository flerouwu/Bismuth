package dev.flero.bismuth.ui;

public interface Widget {
    void render(int mouseX, int mouseY, int x, int y, int width, int height);

    Size getMinimumSize();
}
