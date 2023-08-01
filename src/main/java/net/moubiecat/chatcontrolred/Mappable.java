package net.moubiecat.chatcontrolred;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public interface Mappable<T extends ConfigurationSection> {
    /**
     * 加載
     */
    void onLoad(@NotNull T file);

    /**
     * 重載
     */
    void onReload(@NotNull T file);

    /**
     * 保存
     */
    void onSave(@NotNull T file);
}
