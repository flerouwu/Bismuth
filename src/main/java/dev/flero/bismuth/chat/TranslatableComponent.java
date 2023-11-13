package dev.flero.bismuth.chat;

import net.minecraft.util.Language;

public class TranslatableComponent extends Component {
    public final String key;
    public final Object[] args;

    public TranslatableComponent(String key, Object... args) {
        this.key = key;
        this.args = args;
    }

    @Override
    public String getUnformatted() {
        Language language = Language.getInstance();
        String translated = language.translate(key);
        return Component.injectArguments(translated, args);
    }
}
