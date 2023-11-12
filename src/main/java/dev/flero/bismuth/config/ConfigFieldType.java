package dev.flero.bismuth.config;

import com.moandjiezana.toml.Toml;
import dev.flero.bismuth.TriFunction;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

public enum ConfigFieldType {
    STRING(String.class, String.class.getTypeName(), (toml, key) -> toml.getString(key)),
    BOOLEAN(Boolean.class, Boolean.class.getTypeName(), (toml, key) -> toml.getBoolean(key)),
    BOOLEAN_PRIMITIVE(boolean.class, boolean.class.getTypeName(), (toml, key) -> toml.getBoolean(key)),

    // Numbers
    INTEGER(Integer.class, Integer.class.getTypeName(), (toml, key) -> toml.getLong(key).intValue()),
    INTEGER_PRIMITIVE(int.class, int.class.getTypeName(), (toml, key) -> toml.getLong(key).intValue()),
    LONG(Long.class, Long.class.getTypeName(), (toml, key) -> toml.getLong(key)),
    LONG_PRIMITIVE(long.class, long.class.getTypeName(), (toml, key) -> toml.getLong(key)),

    // Floating point types
    DOUBLE(Double.class, Double.class.getTypeName(), (toml, key) -> toml.getDouble(key)),
    DOUBLE_PRIMITIVE(double.class, double.class.getTypeName(), (toml, key) -> toml.getDouble(key)),
    FLOAT(Float.class, Float.class.getTypeName(), (toml, key) -> toml.getDouble(key).floatValue()),
    FLOAT_PRIMITIVE(float.class, float.class.getTypeName(), (toml, key) -> toml.getDouble(key).floatValue()),

    // Enum
    ENUM(Enum.class, Enum.class.getTypeName(), (toml, type, key) -> {
        if (!type.isEnum()) throw new RuntimeException("this is literally impossible what");
        String value = toml.getString(key);
        //noinspection unchecked,rawtypes
        return Enum.valueOf((Class<? extends Enum>) type, value);
    }),

    /**
     * Minecraft's {@link Identifier}. Stored as a table with two keys: namespace and path.
     */
    IDENTIFIER(Identifier.class, Identifier.class.getTypeName(), (toml, key) -> {
        Toml table = toml.getTable(key);
        return new Identifier(table.getString("namespace"), table.getString("path"));
    });

    /**
     * The class that this type represents.
     * <p>
     * Contrary to popular belief (aka me for 6 fucking hours) THIS IS NOT THE FIELD TYPE.
     * THIS IS THE CLASS THAT THE GETTER IS HANDLING.
     * </p>
     *
     * <p>
     * Now, that I lost a load of braincells trying to figure that shit out, you can instead take
     * in 3 arguments for your getter. (toml, FIELDTYPE_THIS_IS_WHAT_YOU_WANT, key). Have fun.
     * </p>
     *
     * <em>fml</em>
     */
    public final Class<?> clazz;
    public final String typeName;
    public final TriFunction<Toml, Class<?>, String, ?> getter;

    <T> ConfigFieldType(Class<T> clazz, String typeName, BiFunction<Toml, String, T> converter) {
        this(clazz, typeName, (toml, type, key) -> converter.apply(toml, key));
    }

    <T> ConfigFieldType(Class<T> clazz, String typeName, TriFunction<Toml, Class<?>, String, T> converter) {
        this.clazz = clazz;
        this.typeName = typeName;
        this.getter = converter;
    }

    public static @Nullable ConfigFieldType byType(Class<?> clazz) {
        // If it's an enum, return ENUM
        if (clazz.isEnum()) return ENUM;

        // Otherwise, look up with the rest of the values
        for (ConfigFieldType type : values()) {
            if (type.clazz.equals(clazz)) return type;
        }

        // Finally, return null for everything else
        return null;
    }

    public Object getValue(Toml toml, Class<?> type, String key) {
        return getter.apply(toml, type, key);
    }
}
