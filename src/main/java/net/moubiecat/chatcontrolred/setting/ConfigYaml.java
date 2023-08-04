package net.moubiecat.chatcontrolred.setting;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public final class ConfigYaml extends Yaml {
    /**
     * 建構子
     *
     * @param plugin 插件
     */
    public ConfigYaml(@NotNull Plugin plugin) {
        super(plugin, "config.yml");
    }

    /**
     * 取得頻道列表
     *
     * @return 頻道列表
     */
    @NotNull
    public Set<String> getChannels() {
        final ConfigurationSection channels = this.getConfiguration().getConfigurationSection("Channels");
        return channels != null ? channels.getKeys(false) : Set.of();
    }

    /**
     * 取得頻道名稱
     *
     * @param prefix 頻道前綴
     * @return 頻道名稱
     */
    @NotNull
    public String getChannel(@NotNull String prefix) {
        return this.getConfiguration().getString("Channels." + prefix, prefix);
    }

    /**
     * 取得頻道物品
     *
     * @param prefix 頻道前綴
     * @return 頻道物品
     */
    @NotNull
    public Material getChannelMaterial(@NotNull String prefix) {
        return Material.valueOf(this.getConfiguration().getString(prefix + ".Material", "AIR"));
    }

    /**
     * 取得頻道顯示名稱
     *
     * @param prefix 頻道名稱
     * @return 頻道顯示名稱
     */
    @NotNull
    public String getChannelDisplay(@NotNull String prefix) {
        return this.getConfiguration().getString(prefix + ".Display", prefix);
    }

    /**
     * 取得頻道顯示說明
     *
     * @param prefix 頻道名稱
     * @return 頻道顯示名稱
     */
    @NotNull
    public List<String> getChannelLore(@NotNull String prefix) {
        return this.getConfiguration().getStringList(prefix + ".Lore");
    }

    /**
     * 取得預設頻道
     *
     * @return 預設頻道
     */
    @NotNull
    public String getDefaultChannel() {
        return this.getConfiguration().getString("DefaultChannel", "global");
    }

    /**
     * 取得預設頻道物品
     *
     * @return 頻道物品
     */
    @NotNull
    public Material getDefaultChannelMaterial() {
        return Material.valueOf(this.getConfiguration().getString("Default.Material", "AIR"));
    }

    /**
     * 取得預設頻道顯示名稱
     *
     * @return 頻道顯示名稱
     */
    @NotNull
    public String getDefaultChannelDisplay() {
        return this.getConfiguration().getString("Default.Display", "global");
    }

    /**
     * 取得預設頻道顯示說明
     *
     * @return 頻道顯示說明
     */
    @NotNull
    public List<String> getDefaultChannelLore() {
        return this.getConfiguration().getStringList("Default.Lore");
    }

    /**
     * 獲取要覆蓋的指令別名
     *
     * @return 指令別名
     */
    @NotNull
    public List<String> getCommandAliases() {
        return this.getConfiguration().getStringList("CommandAliases");
    }
}
