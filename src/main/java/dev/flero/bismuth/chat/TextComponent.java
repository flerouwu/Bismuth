package dev.flero.bismuth.chat;

public class TextComponent extends Component {
    public final String text;
    public final Object[] args;

    public TextComponent(String text, Object... args) {
        this.text = text;
        this.args = args;
    }

    @Override
    public String getUnformatted() {
        String text = this.text;
        return Component.injectArguments(text, args);
    }
}
