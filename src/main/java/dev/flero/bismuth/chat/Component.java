package dev.flero.bismuth.chat;

import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public abstract class Component {
    public final List<Formatting> styles = new ArrayList<>();
    public final List<Component> children = new ArrayList<>();

    /**
     * Injects arguments into a string.
     * Each argument should have a <code>%s</code> in the string.
     *
     * @param text The string to inject into.
     * @param args The arguments to inject.
     * @return The injected string.
     */
    public static String injectArguments(String text, Object[] args) {
        for (Object arg : args) {
            String replacement;
            if (arg instanceof Component) {
                Component component = (Component) arg;
                replacement = component.getUnformatted();
            } else {
                replacement = arg.toString();
            }

            text = text.replaceFirst("%s", replacement);
        }

        return text;
    }

    /**
     * Returns a new {@link TranslatableComponent} instance.
     *
     * @param key  The key to use for translating.
     * @param args Arguments to inject into the final text.
     * @return The new instance of {@link TranslatableComponent}
     */
    public static TranslatableComponent translated(String key, Object... args) {
        return new TranslatableComponent(key, args);
    }

    /**
     * Returns a new {@link TextComponent} instance.
     *
     * @param text The text to use.
     * @param args Arguments to inject into the final text.
     * @return The new instance of {@link TextComponent}
     */
    public static TextComponent text(String text, Object... args) {
        return new TextComponent(text, args);
    }

    /**
     * Returns a new {@link TextComponent} instance with a content of <code>\n</code>.
     *
     * @return The new instance of {@link TextComponent}
     */
    public static TextComponent newLine() {
        return new TextComponent("\n");
    }

    /**
     * Styles this component with a {@link Formatting} style.
     *
     * @param style The style to apply.
     * @return This component.
     */
    public Component style(Formatting style) {
        this.styles.add(style);
        return this;
    }

    /**
     * Appends a child component to this component.
     *
     * @param child The child component to append.
     * @return This component.
     */
    public Component append(Component child) {
        this.children.add(child);
        return this;
    }

    /**
     * Returns the unformatted text of this component.
     * **Note** this does not include children.
     *
     * @return The unformatted text.
     * @see #getFormatted()
     */
    public abstract String getUnformatted();

    /**
     * Returns the formatted text of this component.
     *
     * @return The formatted text.
     */
    public String getFormatted() {
        StringBuilder unformatted = new StringBuilder(getUnformatted());

        // Styles
        for (Formatting style : styles) {
            unformatted.insert(0, style.toString());
        }

        // Children
        for (Component child : children) {
            unformatted.append(child.getFormatted());
        }

        // Now it's formatted.
        return unformatted.toString();
    }

    /**
     * Converts this Component into a Minecraft {@link LiteralText} instance.
     *
     * @return The converted {@link LiteralText} instance.
     */
    public LiteralText toText() {
        return new LiteralText(getFormatted());
    }
}
