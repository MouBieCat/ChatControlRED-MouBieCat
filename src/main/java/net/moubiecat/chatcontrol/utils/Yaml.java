package net.moubiecat.chatcontrol.utils;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Yaml {
    protected final Plugin plugin;
    protected final File file;
    protected final YamlConfiguration configuration;

    /**
     * 建構子
     *
     * @param plugin   插件
     * @param fileName 檔案名稱
     */
    public Yaml(@NotNull Plugin plugin, @NotNull String fileName) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), fileName);
        this.saveResource();
        this.configuration = YamlConfiguration.loadConfiguration(this.file);
    }

    /**
     * 載入設定檔
     */
    public final void load() {
        try {
            this.configuration.load(this.file);
        } catch (final FileNotFoundException e) {
            // 如果檔案不存在，則複製檔案
            this.saveResource();
            this.load();
        } catch (final IOException | InvalidConfigurationException e) {
            // 如果檔案讀取失敗，則顯示錯誤訊息
            this.plugin.getLogger().warning("The file " + this.file.getName() + " could not be loaded.");
            Bukkit.getPluginManager().disablePlugin(this.plugin);
        }
    }

    /**
     * 儲存設定檔
     */
    public final void save() {
        try {
            this.configuration.save(this.file);
        } catch (final IOException e) {
            // 如果檔案讀取失敗，則顯示錯誤訊息
            plugin.getLogger().warning("The file " + this.file.getName() + " could not be saved.");
            Bukkit.getPluginManager().disablePlugin(this.plugin);
        }
    }

    /**
     * 判斷檔案是否存在
     *
     * @return 是否存在
     */
    protected final boolean isExists() {
        return this.file.exists();
    }

    /**
     * 保存資源檔
     */
    protected final void saveResource() {
        if (!this.isExists())
            this.plugin.saveResource(this.file.getName(), false);
    }
}
