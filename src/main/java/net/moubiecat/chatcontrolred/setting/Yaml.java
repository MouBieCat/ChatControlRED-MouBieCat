package net.moubiecat.chatcontrolred.setting;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class Yaml {
    private final Plugin plugin;
    private final File file;
    private final YamlConfiguration configuration;

    /**
     * 建構子
     *
     * @param plugin   插件
     * @param fileName 檔案名稱
     */
    public Yaml(@NotNull Plugin plugin, @NotNull String fileName) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), fileName);
        this.plugin.saveResource(fileName, false);
        this.configuration = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * 獲取插件
     *
     * @return 插件
     */
    @NotNull
    protected final Plugin getPlugin() {
        return this.plugin;
    }

    /**
     * 獲取檔案
     *
     * @return 檔案
     */
    @NotNull
    protected final File getFile() {
        return this.file;
    }

    /**
     * 獲取設定檔
     *
     * @return 設定檔
     */
    @NotNull
    protected final YamlConfiguration getConfiguration() {
        return this.configuration;
    }

    /**
     * 載入設定檔
     */
    public final void load() {
        try {
            this.configuration.load(this.file);
        } catch (Exception e) {
            plugin.getLogger().warning("This file " + this.file.getName() + " cannot be loaded!");
        }
    }

    /**
     * 儲存設定檔
     */
    public final void save() {
        try {
            this.configuration.save(this.file);
        } catch (Exception e) {
            plugin.getLogger().warning("This file " + this.file.getName() + " cannot be saved!");
        }
    }
}
