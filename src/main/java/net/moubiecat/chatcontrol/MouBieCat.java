package net.moubiecat.chatcontrol;

import net.moubiecat.chatcontrol.channel.ChannelManager;
import net.moubiecat.chatcontrol.listener.ChatListener;
import net.moubiecat.chatcontrol.listener.CommandListener;
import net.moubiecat.chatcontrol.listener.InventoryListener;
import net.moubiecat.chatcontrol.menu.ChannelMenu;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class MouBieCat extends JavaPlugin implements CommandExecutor {
    private final ConfigYaml configYaml = new ConfigYaml(this);
    private final ChannelYaml channelYaml = new ChannelYaml(this);
    private final MessageYaml messageYaml = new MessageYaml(this);

    private final ChannelManager channelManager = new ChannelManager();

    @Override
    public void onEnable() {
        // 處理頻道載入
        channelManager.getLoader().onLoad(this.channelYaml);

        // 註冊事件
        Bukkit.getPluginManager().registerEvents(new InventoryListener(), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(this.channelManager), this);
        Bukkit.getPluginManager().registerEvents(new CommandListener(this.configYaml), this);

        // 註冊指令
        final PluginCommand command = this.getCommand("MBChatControl");
        if (command != null) command.setExecutor(this);
    }

    @Override
    public void onDisable() {
        // 處理頻道儲存
        channelManager.getLoader().onSave(this.channelYaml);
    }

    public void onReload() {
        // 處理配置檔重載
        configYaml.load();
        channelYaml.load();
        messageYaml.load();

        // 處理頻道重載
        channelManager.getLoader().onReload(this.channelYaml);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // 如果是玩家且沒有輸入參數，則開啟頻道選單
        if (sender instanceof Player player && args.length == 0) {
            ChannelMenu.open(player);
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload") && sender.hasPermission("MBChatControl.reload")) {
            // 如果輸入參數為 reload，則重載插件
            MouBieCat.getInstance().onReload();
            sender.sendMessage("§6The plugin ChatControl-MouBieCat has been reloaded.");
            return true;
        }

        return false;
    }

    /**
     * 取得插件實例
     *
     * @return 插件實例
     */
    public static @NotNull MouBieCat getInstance() {
        return getPlugin(MouBieCat.class);
    }

    /**
     * 取得頻道管理器
     *
     * @return 頻道管理器
     */
    @NotNull
    public ChannelManager getChannelManager() {
        return channelManager;
    }

    /**
     * 取得設定檔訊息並發送給玩家
     *
     * @param player  玩家
     * @param message 訊息
     */
    public void sendMessages(@NotNull Player player, @NotNull MessageYaml.Message message, Object... args) {
        this.messageYaml.sendFormatMessage(player, message, args);
    }
}
