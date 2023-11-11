package dev.flero.bismuth.ui.layout;

public class Padding {
    public int left = 0;
    public int right = 0;

    public int top = 0;
    public int bottom = 0;

    public int between = 0;

    public Padding left(int padding) {
        this.left = padding;
        return this;
    }

    public Padding right(int padding) {
        this.right = padding;
        return this;
    }

    public Padding top(int padding) {
        this.top = padding;
        return this;
    }

    public Padding bottom(int padding) {
        this.bottom = padding;
        return this;
    }

    public Padding between(int padding) {
        this.between = padding;
        return this;
    }

    /**
     * Adds padding to all sides, excluding `between`.
     *
     * @param padding The amount of padding to add.
     * @return This instance.
     */
    public Padding all(int padding) {
        left(padding);
        right(padding);
        top(padding);
        bottom(padding);
        return this;
    }
}
