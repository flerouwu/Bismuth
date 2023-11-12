package dev.flero.bismuth.config;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import dev.flero.bismuth.config.exceptions.*;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public class ConfigManager {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static final Logger logger = LogManager.getLogger("Bismuth/ConfigManager");

    public Set<Class<?>> configs = new HashSet<>();

    /**
     * Loads and adds the class to the config manager.
     *
     * @param clazz The class to add
     */
    public void add(Class<?> clazz) throws ConfigException {
        loadConfig(clazz);
        configs.add(clazz);
    }

    /**
     * Saves all configs loaded by this ConfigManager.
     */
    public void saveAll() throws ConfigException {
        for (Class<?> clazz : configs)
            saveConfig(clazz);
    }

    /**
     * Load config for a module.
     * The class must be annotated with {@link ConfigHolder}.
     *
     * @throws ConfigException if the config is invalid
     */
    public void loadConfig(Class<?> clazz) throws ConfigException {
        // Class must be annotated with ConfigHolder
        ConfigHolder holder = getHolder(clazz);
        logger.info("Loading config {}", holder.name());

        // Get and validate config file
        File file = getFile(clazz);

        // read file and set values to fields
        // we ignore missing keys, as the fields should have default values
        // fields will write their values, no matter if they are default when the config is saved
        Toml toml = new Toml().read(file);
        for (Field field : Arrays.stream(clazz.getFields()).filter(f -> f.isAnnotationPresent(ConfigValue.class)).toArray(Field[]::new)) {
            if (toml.contains(field.getName())) {
                ConfigFieldType configField = ConfigFieldType.byType(field.getType());

                // Throw if configField is empty
                if (configField == null)
                    throw new InvalidFieldTypeException(String.format(
                            "Field %s is of invalid type %s",
                            field.getName(),
                            field.getType().getTypeName()
                    ));

                // Get the value
                Object value;
                try {
                    value = configField.getValue(toml, field.getType(), field.getName());
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

    /**
     * Saves the config for a module.
     * The class must be annotated with {@link ConfigHolder}.
     *
     * @param clazz the class to save the config for
     * @throws NoConfigHolderException if the class is not annotated with {@link ConfigHolder}
     * @throws ConfigException         if there was an issue with saving
     */
    public void saveConfig(Class<?> clazz) throws ConfigException {
        ConfigHolder holder = getHolder(clazz);
        logger.info("Saving config {}", holder.name());

        // Get and validate config file
        File file = getFile(clazz);

        // Write config
        Map<String, Object> map = new HashMap<>();
        for (Field field : clazz.getFields()) {
            if (!field.isAnnotationPresent(ConfigValue.class))
                // Skip fields that aren't annotated with ConfigValue
                continue;

            try {
                map.put(field.getName(), field.get(clazz));
            } catch (IllegalAccessException exception) {
                throw new FieldReadException("Failed to read field " + field.getName(), exception);
            }
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("# Bismuth Configuration File\n");
            writer.write("# --------------------------\n");
            writer.write("# Class: " + clazz.getName() + "\n");
            writer.write("# Fields Written: " + map.size() + "\n");
            writer.write("# Written at: " + new Date() + "\n");
            writer.write("# --------------------------\n\n");
            new TomlWriter().write(map, writer);
        } catch (IOException exception) {
            throw new UnableToWriteConfigException("Unable to write config file for module " + holder.name(), exception);
        }
    }

    private ConfigHolder getHolder(Class<?> clazz) throws NoConfigHolderException {
        ConfigHolder holder = clazz.getAnnotation(ConfigHolder.class);
        if (holder == null)
            throw new NoConfigHolderException("Unable to get @ConfigHolder for class " + clazz.getName());

        return holder;
    }

    @NotNull
    private File getFile(Class<?> clazz) throws UnableToCreateConfigException {
        ConfigHolder holder = getHolder(clazz);
        File file = new File(client.runDirectory, String.format("config/bismuth/%s.toml", holder.name()));
        if (!file.exists()) {
            // if file doesn't exist, create it and write default field values
            try {
                if (!file.getParentFile().mkdirs() && !file.createNewFile())
                    throw new IOException("mkdirs() and createNewFile() both returned false");

                saveConfig(clazz);
            } catch (IOException exception) {
                throw new UnableToCreateConfigException("Unable to create config file for module " + holder.name(), exception);
            }
        }

        return file;
    }
}
