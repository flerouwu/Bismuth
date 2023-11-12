package dev.flero.bismuth.config;

import com.moandjiezana.toml.Toml;
import net.minecraft.util.Identifier;

import java.util.function.BiFunction;

public enum ConfigFieldType {
    STRING(String.class, String.class.getTypeName(), Toml::getString),
    BOOLEAN(Boolean.class, Boolean.class.getTypeName(), Toml::getBoolean),

    // Numbers
    INTEGER(Integer.class, Integer.class.getTypeName(), (toml, key) -> toml.getLong(key).intValue()),
    LONG(Long.class, Long.class.getTypeName(), Toml::getLong),

    // Floating point types
    DOUBLE(Double.class, Double.class.getTypeName(), Toml::getDouble),
    FLOAT(Float.class, Float.class.getTypeName(), (toml, key) -> toml.getDouble(key).floatValue()),

    /**
     * Minecraft's {@link Identifier}. Stored as a table with two keys: namespace and path.
     */
    IDENTIFIER(Identifier.class, Identifier.class.getTypeName(), (toml, key) -> {
        Toml table = toml.getTable(key);
        return new Identifier(table.getString("namespace"), table.getString("path"));
    });

    public final Class<?> clazz;
    public final String typeName;
    public final BiFunction<Toml, String, Object> converter;

    ConfigFieldType(Class<?> clazz, String typeName, BiFunction<Toml, String, Object> converter) {
        this.clazz = clazz;
        this.typeName = typeName;
        this.converter = converter;
    }
}
