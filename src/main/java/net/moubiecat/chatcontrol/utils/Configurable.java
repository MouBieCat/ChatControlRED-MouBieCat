package net.moubiecat.chatcontrol.utils;

import org.jetbrains.annotations.NotNull;

public interface Configurable<T extends Yaml> {
    /**
     * Load config
     *
     * @param config config
     */
    default void onLoad(@NotNull T config) {
    }

    /**
     * Save config
     *
     * @param config config
     */
    default void onSave(@NotNull T config) {
    }

    /**
     * Reload config
     *
     * @param config config
     */
    default void onReload(@NotNull T config) {
    }
}
