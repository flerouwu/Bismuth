package dev.flero.bismuth.modules

import dev.flero.bismuth.BismuthMod
import dev.flero.bismuth.config.ConfigHolder
import dev.flero.bismuth.config.ConfigValue
import dev.flero.bismuth.i18n.I18n
import dev.flero.bismuth.i18n.annotations.Translatable
import dev.flero.bismuth.i18n.annotations.TranslationHolder

class ScreenshotManager(mod: BismuthMod) {
    init {
        // should make it similar to [I18n]
        mod.config.loadConfig(Config::class.java)
        Translations.load()
    }

    @TranslationHolder(file = "screenshots")
    object Translations {
        fun load() {
            I18n.instance.translate(this::class)
            Actions.load()
        }

        @Translatable(id = "message", description = "", requiresRestart = false)
        lateinit var message: String

        fun getFormattedMessage(): String {
            return message
                .replace("{copy}", Actions.copy)
                .replace("{open}", Actions.open)
                .replace("{delete}", Actions.delete)
        }

        @TranslationHolder(file = "screenshots", table = "actions")
        object Actions {
            fun load() = I18n.instance.translate(this::class)

            @Translatable(id = "copy", description = "", requiresRestart = false)
            lateinit var copy: String

            @Translatable(id = "open", description = "", requiresRestart = false)
            lateinit var open: String

            @Translatable(id = "delete", description = "", requiresRestart = false)
            lateinit var delete: String
        }
    }

    @ConfigHolder(name = "modules/screenshots")
    object Config {
        @ConfigValue
        var isEnabled = true

        @ConfigValue
        var deleteConfirmation = true

        @ConfigValue
        var confirmTimeoutSeconds = 5
    }
}