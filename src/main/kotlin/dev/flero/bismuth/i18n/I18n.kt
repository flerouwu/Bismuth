package dev.flero.bismuth.i18n

import com.moandjiezana.toml.Toml
import dev.flero.bismuth.i18n.I18n.Companion.loadLanguage
import dev.flero.bismuth.i18n.annotations.Translatable
import dev.flero.bismuth.i18n.annotations.TranslationHolder
import net.minecraft.client.MinecraftClient
import org.apache.logging.log4j.LogManager
import java.io.File
import java.io.FileWriter
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

/**
 * Represents a translation for I18n support.
 * ---
 *
 * **Notes**
 * > This class has a private constructor, and can't be initialized directly.
 * > Use [loadLanguage] to get a language. It will return the fallback of [I18nTag.EnglishUnitedStates]
 * > if the translation files for the requested [I18nTag] doesn't exist.
 *
 * > This helps prevent duplicate translations being loaded.
 */
class I18n {
    companion object {
        private val client = MinecraftClient.getInstance()
        private val logger = LogManager.getLogger("Bismuth/I18nKt")

        lateinit var instance: I18n
        val fallback: I18n = I18n(I18nTag.EnglishUnitedStates)

        /**
         * Loads a [I18nTag] into [instance].
         */
        fun loadLanguage(tag: I18nTag) {
            // Return instance if it has the requested tag
            if (this::instance.isInitialized && instance.tag == tag) return

            // Prevent duplicate translations
            if (tag == I18nTag.EnglishUnitedStates) {
                instance = fallback
                return
            }

            // Load language
            instance = if (this::instance.isInitialized) I18n(tag, instance.translated)
            else I18n(tag)
        }
    }

    private val directory: File
    val name: String
    val tag: I18nTag

    /**
     * A list of classes that this language has already translated.
     */
    val translated: Set<KClass<*>> = mutableSetOf()

    /**
     * Creates a new [I18n] instance for [tag].
     * Doesn't re-translate any classes. Use the other
     * constructor to re-translate classes.
     *
     * @param tag the tag of the language
     */
    private constructor(tag: I18nTag) : this(tag, emptySet())

    /**
     * Creates a new [I18n] instance for [tag].
     * Automatically re-translates all classes in [translated].
     *
     * @param tag the tag of the language
     * @param translated the previous list of translated classes
     */
    private constructor(tag: I18nTag, translated: Set<KClass<*>>) {
        this.tag = tag
        directory = client.runDirectory.resolve("config/bismuth/lang/${tag.getTag()}")
        if (!directory.exists() && !directory.mkdirs()) throw CreateLanguageDirectory(tag)

        // Read info file
        logger.info("Loading info.toml for $tag")
        val info: File = directory.resolve("info.toml")
        if (!info.exists()) writeDefaults(info)

        // Load info toml
        val toml = Toml().read(info)
        name = toml.getString("name") ?: throw InvalidInfoFile("Missing 'name' key.")

        // Reload translations
        if (translated.isNotEmpty()) {
            logger.info("Reloading translations for ${translated.size} in $tag...")
            for (clazz in translated) {
                try {
                    translate(clazz)
                } catch (exception: Exception) {
                    logger.error(
                        "Unable to reload translation for $clazz. It won't be updated with the new translation.",
                        exception
                    )
                }
            }
        }
    }

    /**
     * Attempts to translate an object class' static properties,
     * and it's inner object classes (annotated with [TranslationHolder]).
     *
     * Each property must be annotated with [Translatable].
     * The object class must be annotated with [TranslationHolder].
     *
     * @throws ClassMustBeObject if [clazz] isn't an object class
     * @throws NoTranslationHolder if [clazz] doesn't have a [TranslationHolder] annotation
     */
    fun translate(clazz: KClass<*>): KClass<*> {
        // Throw error if clazz doesn't have an objectInstance
        if (clazz.objectInstance == null) throw ClassMustBeObject(clazz)

        // Return loaded translation if it exists.
        if (translated.contains(clazz)) {
            logger.info("Translation for $clazz already exists. Returning...")
            return translated.find { it == clazz }!!
        }

        // Get holder
        val holder = clazz.findAnnotation<TranslationHolder>() ?: throw NoTranslationHolder(clazz)
        logger.info("Translating $clazz for $tag...")

        // Validate file or write defaults
        val file = File(directory, "${holder.file}.toml")
        if (!file.exists()) {
            logger.info("Writing defaults for $clazz in $tag")
            writeDefaults(file)
        }

        // Load toml
        var toml = Toml().read(file)
        if (holder.table.isNotEmpty()) {
            for (name in holder.table.split(".")) {
                toml = toml.getTable(name) ?: throw NoSubTableFound(clazz, holder.table)
            }
        }

        // Load translations
        logger.info("Property size: ${clazz.memberProperties.size}")
        for (property in clazz.memberProperties) {
            logger.info("Translating ${property.name} in $clazz. Annotation: ${property.annotations}")
            val translatable = property.findAnnotation<Translatable>() ?: continue
            val id = translatable.id.ifEmpty { property.name }

            if (property !is KMutableProperty<*>) {
                logger.warn("$id in $clazz isn't mutable. Unable to set value. Skipping...")
                continue
            }

            val value = toml.getString(id, null)
            if (value == null) {
                // Attempt to get value from fallback
                if (fallback == this) {
                    logger.warn("Unable to get value for $id in $clazz from fallback (we are fallback). Skipping...")
                    continue
                }

                logger.warn("Using fallback translation to transfer $id in $clazz")
                @Suppress("DEPRECATION") // [fallback.translate]
                property.setter.call(clazz.objectInstance, fallback.translate(file, id))
            } else {
                logger.info("Setting value for $id in $clazz")
                property.setter.call(clazz.objectInstance, value)
            }
        }

        // Cache translation and return class
        translated.plus(clazz)
        return clazz
    }

    /**
     * Returns translated string with [id] from [file].
     *
     * **Note:** this creates a new [Toml] instance every time,
     * and thus is not recommended for use multiple times. It is
     * recommended to use your own [Toml] instance.
     *
     * @param file the file to retrieve the translation from
     * @param id the id of the translation
     * @return the translated string, or null if it doesn't exist
     */
    @Deprecated("Use your own Toml instance instead. See Javadoc for more info.")
    fun translate(file: File, id: String): String? {
        val toml = Toml().read(file)
        return toml.getString(id)
    }

    /**
     * Writes the default contents for [file].
     * This includes a header and all the default values.
     *
     * If [file] already exists, it will be renamed to [file].bak
     * @throws FileAlreadyExistsException if [file] exists AND [file].bak exists
     */
    private fun writeDefaults(file: File) {
        if (file.exists()) {
            logger.warn("writeDefaults() called on an existing file. Backing up original file and overwriting contents...")
            if (!file.renameTo(File(file.absolutePath + ".bak")))
                throw FileAlreadyExistsException(
                    file,
                    reason = "Unable to rename file ${file.absolutePath} to ${file.absolutePath}.bak"
                )
        }

        file.createNewFile() || throw RuntimeException("Unable to create file ${file.absolutePath}")
        FileWriter(file).use { writer ->
            writeHeader(writer)
            writer.write("\n")

            // Write file content from jar file
            this::class.java.getResourceAsStream("/lang/${tag.getTag()}/${file.name}")
                .use { stream ->
                    if (stream == null) throw RuntimeException("Unable to get stream for ${file.name}")
                    stream.reader().use { reader ->
                        reader.copyTo(writer)
                    }
                }
        }
    }

    /**
     * Writes a nice lil header to the file.
     * Contains information such as what the file is, how you can edit
     * it, and when it was generated. Also contains a link for users
     * to help translate the mod.
     */
    private fun writeHeader(writer: FileWriter) {
        writer.write(
            """
            # Bismuth Translation File
            #
            # Generated at ${Date()} for ${tag.getTag()}
            #
            # ------------------------
            #
            # Note: it is recommended to edit the translations of Bismuth
            # using the in-game editor. For more info, visit
            #       https://github.com/flerouwu/Bismuth/wiki/Translations
            #
            # ------------------------
            # 
            # This file is used for translating Bismuth into other languages.
            # You can also provide your own text to customise Bismuth's messages.
            #
            # To translate Bismuth, simply replace the text on the right of the equals sign.
            # For example, if you wanted to translate "Hello, world!" to "Bonjour, le monde!",
            # you would change the line to:
            #       translationKey = "Bonjour, le monde!"
            #
            #
            # It's recommended to visit the wiki to learn more information.
            #       https://github.com/flerouwu/Bismuth/wiki/Translations
            #
            # -------------------------
            
        """.trimIndent()
        )
    }
}