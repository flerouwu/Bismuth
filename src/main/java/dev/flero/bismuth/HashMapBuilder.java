package dev.flero.bismuth;

import java.util.HashMap;

public class HashMapBuilder<K, V> extends HashMap<K, V> {
    public HashMapBuilder<K, V> putSelf(K key, V value) {
        super.put(key, value);
        return this;
    }
}
