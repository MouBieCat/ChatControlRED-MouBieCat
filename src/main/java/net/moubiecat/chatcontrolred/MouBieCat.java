package net.moubiecat.chatcontrolred;

import net.moubiecat.chatcontrolred.channel.ChannelManager;
import net.moubiecat.chatcontrolred.command.MBChatControl;
import net.moubiecat.chatcontrolred.listener.ChatListener;
import net.moubiecat.chatcontrolred.listener.CommandListener;
import net.moubiecat.chatcontrolred.listener.InventoryListener;
import net.moubiecat.chatcontrolred.setting.ConfigYaml;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class MouBieCat extends JavaPlugin {
    private ConfigYaml configYaml;

    private final ChannelManager channelManager = new ChannelManager();

    @Override
    public void onEnable() {
        // 映射配置文件
        this.configYaml = new ConfigYaml(this);

        // 載入配置文件
        this.channelManager.onLoad(this.configYaml);

        // 註冊指令
        final PluginCommand mainCommand = this.getCommand("mbchatcontrol");
        if (mainCommand != null)
            mainCommand.setExecutor(new MBChatControl());

        // 註冊事件監聽器
        this.getServer().getPluginManager().registerEvents(new ChatListener(), this);
        this.getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        this.getServer().getPluginManager().registerEvents(new CommandListener(this.configYaml), this);
    }

    @Override
    public void onDisable() {
        // 處理 channelManager 的配置文件
        this.channelManager.onSave(this.configYaml);

        // 保存配置文件
        this.configYaml.save();
    }

    public void onReload() {
        // 重載配置文件
        this.configYaml.load();

        // 處理 channelManager 的配置文件
        this.channelManager.onReload(this.configYaml);
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
