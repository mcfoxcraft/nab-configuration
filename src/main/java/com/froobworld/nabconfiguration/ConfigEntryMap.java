package com.froobworld.nabconfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class ConfigEntryMap<K, T> {
    private final Function<K, String> keyMappingFunction;
    private final Map<String, ConfigEntry<T>> entryMap = new HashMap<>();
    private final Supplier<ConfigEntry<T>> emptyConfigEntrySupplier;
    private final ConfigEntry<T> def;

    public ConfigEntryMap(Function<K, String> keyMappingFunction, Supplier<ConfigEntry<T>> emptyConfigEntrySupplier) {
        this.keyMappingFunction = keyMappingFunction;
        this.emptyConfigEntrySupplier = emptyConfigEntrySupplier;
        this.def = emptyConfigEntrySupplier.get();
    }

    public ConfigEntry<T> of(K key) {
        return entryMap.getOrDefault(keyMappingFunction.apply(key), def);
    }

    void setDefault(Object def) {
        this.def.setValue(def);
    }

    void put(String key, Object value) {
        ConfigEntry<T> configEntry = emptyConfigEntrySupplier.get();
        configEntry.setValue(value);
        entryMap.put(key, configEntry);
    }

}
