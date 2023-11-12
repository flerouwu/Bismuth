package dev.flero.bismuth.config;

import com.moandjiezana.toml.Toml;
import dev.flero.bismuth.config.exceptions.FieldConversionException;
import dev.flero.bismuth.config.exceptions.FieldWriteException;
import dev.flero.bismuth.config.exceptions.InvalidFieldTypeException;
import dev.flero.bismuth.config.exceptions.UnableToCreateConfigException;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

public class ConfigManager {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static final Logger logger = LogManager.getLogger("Bismuth/ConfigManager");

    @NotNull
    private static File getFile(ConfigHolder holder) {
        File file = new File(client.runDirectory, String.format("config/bismuth/%s.toml", holder.name()));
        if (!file.exists()) {
            // if file doesn't exist, create it and write default field values
            try {
                if (!file.getParentFile().mkdirs() && !file.createNewFile()) {
                    throw new IOException("mkdirs() and createNewFile() both returned false");
                }
            } catch (IOException exception) {
                throw new UnableToCreateConfigException("Unable to create config file for module " + holder.name(), exception);
            }
        }

        return file;
    }

    /**
     * Load config for a module.
     * The class must be annotated with {@link ConfigHolder}.
     */
    public void loadConfig(Class<?> clazz) {
        // Class must be annotated with ConfigHolder
        ConfigHolder holder = clazz.getAnnotation(ConfigHolder.class);
        if (holder == null)
            throw new RuntimeException("Tried to load a config for a class that is not annotated with @ConfigHolder");

        logger.info("Loading config {}", holder.name());

        // Get and validate config file
        File file = getFile(holder);

        // read file and set values to fields
        // we ignore missing keys, as the fields should have default values
        // fields will write their values, no matter if they are default when the config is saved
        Toml toml = new Toml().read(file);
        for (Field field : clazz.getFields()) {
            if (toml.contains(field.getName())) {
                Optional<ConfigFieldType> configField = Arrays.stream(ConfigFieldType.values())
                        .filter(type -> type.typeName.equals(field.getType().getTypeName()))
                        .findFirst();

                // Throw if configField is empty
                if (!configField.isPresent())
                    throw new InvalidFieldTypeException(String.format(
                            "Field %s is of invalid type %s",
                            field.getName(),
                            field.getType().getTypeName()
                    ));

                Object value;
                try {
                    value = configField.get().converter.apply(toml, field.getName());
                } catch (Exception exception) {
                    throw new FieldConversionException(String.format(
                            "Unable to convert field %s to type %s",
                            field.getName(),
                            field.getType().getTypeName()
                    ), exception);
                }

                // Set field
                try {
                    field.set(clazz, value);
                } catch (IllegalAccessException e) {
                    throw new FieldWriteException("Failed to write field " + field.getName(), e);
                }
            }
        }
    }
}
