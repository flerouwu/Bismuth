package dev.flero.bismuth.config;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import dev.flero.bismuth.HashMapBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ConfigManager {
    public static final Path DIRECTORY = FabricLoader.getInstance().getConfigDir().resolve("bismuth").toAbsolutePath().normalize();
    public static final Logger logger = LogManager.getLogger("Bismuth/ConfigManager");

    public static <T> boolean loadConfig(Class<T> clazz) {
        ConfigHolder holder = clazz.getAnnotation(ConfigHolder.class);
        if (holder == null) return false;
        logger.info("Loading config for: " + holder.name());

        List<Field> fields = Arrays.stream(clazz.getFields()).filter(field -> field.isAnnotationPresent(ConfigValue.class)).collect(Collectors.toList());
        if (fields.isEmpty()) return false;

        File file = new File(DIRECTORY.toFile(), holder.name() + ".toml");
        boolean exists = file.exists();
        try {
            if (!createFile(file)) return false;
        } catch (IOException exception) {
            logger.warn(String.format("Failed to create config file: %s. Using default values.", file.getAbsolutePath()), exception);
        }

        if (!exists) {
            // Write default values
            Map<String, Object> defaultValues = new HashMap<>();
            for (Field field : fields) {
                Map.Entry<String, Object> entry = newMapEntry(field.getName());
                try {
                    entry.setValue(field.get(clazz));
                } catch (IllegalAccessException exception) {
                    logger.warn(String.format("Failed to get value of field: %s. Using default value.", field.getName()), exception);
                }

                defaultValues.put(entry.getKey(), entry.getValue());
            }

            // Write defaults to file
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("# Bismuth Configuration File\n");
                writer.write("# Generated at " + new Date() + "\n");
                writer.write("# Generated for class " + clazz.getName() + "\n\n");
                new TomlWriter().write(defaultValues, writer);
            } catch (IOException exception) {
                logger.fatal(String.format("Failed to write config file: %s. Using default values.", file.getAbsolutePath()));
            }
        }

        // Read from file
        Toml toml = new Toml();
        try {
            toml.read(file);
        } catch (IllegalStateException exception) {
            logger.fatal(String.format("Invalid TOML configuration file: %s", file.getAbsolutePath()), exception);
        }

        // Write to fields
        for (Field field : fields) {
            Type fieldType = field.getType();
            HashMap<Predicate<Type>, Supplier<Object>> matchFunctions = new HashMapBuilder<Predicate<Type>, Supplier<Object>>()
                    // String
                    .putSelf(
                            type -> type.getTypeName().equals(String.class.getTypeName()),
                            () -> toml.getString(field.getName())
                    )
                    // Integer
                    .putSelf(
                            type -> type.getTypeName().equals(Integer.class.getTypeName()) || type.getTypeName().equals(int.class.getTypeName()),
                            () -> toml.getLong(field.getName()).intValue()
                    )
                    // Long
                    .putSelf(
                            type -> type.getTypeName().equals(Long.class.getTypeName()) || type.getTypeName().equals(long.class.getTypeName()),
                            () -> toml.getLong(field.getName())
                    )
                    // Float
                    .putSelf(
                            type -> type.getTypeName().equals(Float.class.getTypeName()) || type.getTypeName().equals(float.class.getTypeName()),
                            () -> toml.getDouble(field.getName()).floatValue()
                    )
                    // Double
                    .putSelf(
                            type -> type.getTypeName().equals(Double.class.getTypeName()) || type.getTypeName().equals(double.class.getTypeName()),
                            () -> toml.getDouble(field.getName())
                    )
                    // Boolean
                    .putSelf(
                            type -> type.getTypeName().equals(Boolean.class.getTypeName()) || type.getTypeName().equals(boolean.class.getTypeName()),
                            () -> toml.getBoolean(field.getName())
                    )
                    // List
                    .putSelf(
                            type -> type.getTypeName().equals(List.class.getTypeName()),
                            () -> toml.getList(field.getName())
                    )
                    // Set
                    .putSelf(
                            type -> type.getTypeName().equals(Set.class.getTypeName()),
                            () -> toml.getList(field.getName())
                    )
                    // Map
                    .putSelf(
                            type -> type.getTypeName().equals(Map.class.getTypeName()),
                            () -> toml.getTable(field.getName())
                    )
                    // Identifier
                    .putSelf(
                            type -> type.getTypeName().equals(Identifier.class.getTypeName()),
                            () -> {
                                Toml table = toml.getTable(field.getName());
                                return new Identifier(table.getString("namespace"), table.getString("path"));
                            }
                    );

            try {
                field.set(clazz, match(fieldType, matchFunctions, (type) -> {
                    throw new RuntimeException("Invalid field type: " + type.getTypeName());
                }));
            } catch (IllegalAccessException exception) {
                logger.warn(String.format("Failed to set value of field: %s. Using default value.", field.getName()), exception);
            }
        }

        return true;
    }

    public static <T> Object match(T value, Map<Predicate<T>, Supplier<Object>> map, Consumer<T> defaultFunction) {
        for (Map.Entry<Predicate<T>, Supplier<Object>> entry : map.entrySet()) {
            if (entry.getKey().test(value)) {
                return entry.getValue().get();
            }
        }

        defaultFunction.accept(value);
        return null;
    }

    private static boolean createFile(File file) throws IOException {
        if (file.exists()) return true;

        File parent = file.getParentFile();
        if (!parent.exists()) {
            if (!parent.mkdirs()) return false;
        }

        return file.exists() || file.createNewFile();
    }

    private static <K, V> Map.Entry<K, V> newMapEntry(K key) {
        return new Map.Entry<K, V>() {
            private final K mapKey = key;
            private V mapValue = null;

            @Override
            public K getKey() {
                return this.mapKey;
            }

            @Override
            public V getValue() {
                return this.mapValue;
            }

            @Override
            @SuppressWarnings("unchecked")
            public V setValue(Object o) {
                return this.mapValue = (V) o;
            }
        };
    }
}
