package net.moubiecat.chatcontrol;

import com.google.inject.Guice;
import com.google.inject.Injector;
import jdk.jfr.Description;
import net.moubiecat.chatcontrol.injector.ChannelManager;
import net.moubiecat.chatcontrol.injector.ChannelYaml;
import net.moubiecat.chatcontrol.injector.ConfigYaml;
import net.moubiecat.chatcontrol.injector.MessageYaml;
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
    @Description("Guice 注入器")
    private static Injector injector = null;

    private final ChannelManager channelManager = new ChannelManager();
    private final ConfigYaml configYaml = new ConfigYaml(MouBieCat.this);
    private final ChannelYaml channelYaml = new ChannelYaml(MouBieCat.this);
    private final MessageYaml messageYaml = new MessageYaml(MouBieCat.this);

    public MouBieCat() {
        // 註冊 Guice 注入器
        injector = Guice.createInjector(binder -> {
            // 註冊插件實例
            binder.bind(MouBieCat.class).toInstance(this);
            // 註冊配置檔
            binder.bind(ConfigYaml.class).toInstance(MouBieCat.this.configYaml);
            binder.bind(ChannelYaml.class).toInstance(MouBieCat.this.channelYaml);
            binder.bind(MessageYaml.class).toInstance(MouBieCat.this.messageYaml);
            // 註冊頻道管理器
            binder.bind(ChannelManager.class).toInstance(MouBieCat.this.channelManager);
        });
    }

    @Override
    public void onEnable() {
        // 處理頻道配置檔載入
        this.channelManager.getParser().onLoad(this.channelYaml);
        // 註冊事件
        Bukkit.getPluginManager().registerEvents(injector.getInstance(InventoryListener.class), this);
        Bukkit.getPluginManager().registerEvents(injector.getInstance(ChatListener.class), this);
        Bukkit.getPluginManager().registerEvents(injector.getInstance(CommandListener.class), this);
        // 註冊指令
        final PluginCommand command = this.getCommand("MBChatControl");
        if (command != null) command.setExecutor(this);
    }

    @Override
    public void onDisable() {
        // 處理頻道儲存
        channelManager.getParser().onSave(this.channelYaml);
    }

    public void onReload() {
        // 處理配置檔重載
        configYaml.load();
        channelYaml.load();
        messageYaml.load();
        // 處理頻道重載
        channelManager.getParser().onReload(this.channelYaml);
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
            this.onReload();
            sender.sendMessage("§6The plugin ChatControl-MouBieCat has been reloaded.");
            return true;
        }

        return false;
    }

    /**
     * 取得 Guice 注入器
     *
     * @return Guice 注入器
     */
    @NotNull
    public static Injector getInjector() {
        return MouBieCat.injector;
    }
}
