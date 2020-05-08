package com.froobworld.nabconfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ConfigSectionMap<K, C extends ConfigSection> {
    private final Function<K, String> keyMappingFunction;
    private final Map<String, C> sectionMap = new HashMap<>();
    private final Class<C> entryType;
    private C defaultSection;

    public ConfigSectionMap(Function<K, String> keyMappingFunction, Class<C> entryType) {
        this.keyMappingFunction = keyMappingFunction;
        this.entryType = entryType;
    }


    void setDefaultSection(C defaultSection) {
        this.defaultSection = defaultSection;
    }

    void put(String key, C configSection) {
        sectionMap.put(key, configSection);
    }

    public C of(K key) {
        return sectionMap.getOrDefault(keyMappingFunction.apply(key), defaultSection);
    }

    Class<C> entryType() {
        return entryType;
    }

}
