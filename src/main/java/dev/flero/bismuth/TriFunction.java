package dev.flero.bismuth;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface TriFunction<F, S, T, R> {
    R apply(F first, S second, T third);

    @SuppressWarnings("unused")
    @Contract(pure = true)
    @NotNull
    default <V> TriFunction<F, S, T, V> andThen(@NotNull Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (f, s, t) -> after.apply(this.apply(f, s, t));
    }
}
