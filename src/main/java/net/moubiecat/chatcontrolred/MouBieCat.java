package net.moubiecat.chatcontrolred;

import net.moubiecat.chatcontrolred.channel.ChannelManager;
import net.moubiecat.chatcontrolred.listener.ChatListener;
import net.moubiecat.chatcontrolred.listener.CommandListener;
import net.moubiecat.chatcontrolred.listener.InventoryListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class MouBieCat extends JavaPlugin {
    private final ChannelManager channelManager = new ChannelManager();

    @Override
    public void onEnable() {
        // 映射配置文件
        this.saveDefaultConfig();
        // 處理 channelManager 的配置文件
        channelManager.onLoad(this.getConfig());
        // 註冊事件監聽器
        this.getServer().getPluginManager().registerEvents(new ChatListener(), this);
        this.getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        this.getServer().getPluginManager().registerEvents(new CommandListener(), this);
    }

    @Override
    public void onDisable() {
        // 保存配置文件
        this.saveConfig();
        // 處理 channelManager 的配置文件
        channelManager.onSave(this.getConfig());
    }

    public void onReload() {
        this.reloadConfig();
        // 處理 channelManager 的配置文件
        channelManager.onReload(this.getConfig());
    }

    /**
     * 獲取插件實例
     *
     * @return 插件實例
     */
    @NotNull
    public static MouBieCat getInstance() {
        return getPlugin(MouBieCat.class);
    }

    /**
     * 獲取頻道管理器
     *
     * @return 頻道管理器
     */
    @NotNull
    public ChannelManager getChannelManager() {
        return channelManager;
    }
}
