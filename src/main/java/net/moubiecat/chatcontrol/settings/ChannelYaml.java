package net.moubiecat.chatcontrol.settings;

import net.moubiecat.chatcontrol.settings.Yaml;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public final class ChannelYaml extends Yaml {
    /**
     * 建構子
     *
     * @param plugin 插件
     */
    public ChannelYaml(@NotNull Plugin plugin) {
        super(plugin, "channels.yml");
    }

    public @NotNull Set<String> getChannelNames() {
        final ConfigurationSection list = this.configuration.getConfigurationSection("List");
        return list == null ? Set.of() : list.getKeys(false);
    }

    public @NotNull String getChannelPrefix(@NotNull String name) {
        return this.configuration.getString("List." + name + ".ChannelPrefix", name);
    }

    public @NotNull String getChannelName(@NotNull String name) {
        return this.configuration.getString("List." + name + ".ChannelName", name);
    }

    public @Nullable String getChannelPermission(@NotNull String name) {
        return this.configuration.getString("List." + name + ".ChannelPermission");
    }

    public @NotNull Material getChannelIcon(@NotNull String name) {
        return Material.valueOf(this.configuration.getString("List." + name + ".ItemMaterial", "AIR").toUpperCase());
    }

    public @NotNull String getChannelDisplay(@NotNull String name) {
        return this.configuration.getString("List." + name + ".ItemDisplay", name);
    }

    public @NotNull List<String> getChannelLore(@NotNull String name) {
        return this.configuration.getStringList("List." + name + ".ItemLore");
    }
}
