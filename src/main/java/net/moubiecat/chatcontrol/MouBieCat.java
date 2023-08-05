package net.moubiecat.chatcontrol;

import com.google.inject.Injector;
import net.moubiecat.chatcontrol.database.FirstChatDatabase;
import net.moubiecat.chatcontrol.injector.ChannelManager;
import net.moubiecat.chatcontrol.injector.ChannelYaml;
import net.moubiecat.chatcontrol.injector.ConfigYaml;
import net.moubiecat.chatcontrol.injector.MessageYaml;
import net.moubiecat.chatcontrol.listener.CommandListener;
import net.moubiecat.chatcontrol.listener.InventoryListener;
import net.moubiecat.chatcontrol.listener.PlayerListener;
import net.moubiecat.chatcontrol.menu.ChannelMenu;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class MouBieCat extends JavaPlugin {
    private static final InjectRegistration registration = new InjectRegistration();
    private static Injector injector = null;

    @Override
    public void onLoad() {
        // 註冊一些東西
        registration.register(MouBieCat.class, this);
        registration.register(ConfigYaml.class, new ConfigYaml(this));
        registration.register(ChannelYaml.class, new ChannelYaml(this));
        registration.register(MessageYaml.class, new MessageYaml(this));
        registration.register(ChannelManager.class, new ChannelManager());
        registration.register(FirstChatDatabase.class, new FirstChatDatabase());
        // 生成注入器
        injector = registration.createInjector();
    }

    @Override
    public void onEnable() {
        // 連接資料庫
        injector.getInstance(FirstChatDatabase.class).connect();
        // 加載頻道配置檔
        injector.getInstance(ChannelManager.class)
                .getParser()
                .onLoad(injector.getInstance(ChannelYaml.class));
        // 註冊事件
        Bukkit.getPluginManager().registerEvents(injector.getInstance(InventoryListener.class), this);
        Bukkit.getPluginManager().registerEvents(injector.getInstance(PlayerListener.class), this);
        Bukkit.getPluginManager().registerEvents(injector.getInstance(CommandListener.class), this);
    }

    @Override
    public void onDisable() {
        // 保存頻道配置檔
        injector.getInstance(ChannelManager.class)
                .getParser()
                .onSave(injector.getInstance(ChannelYaml.class));
        // 保存配置檔
        injector.getInstance(ConfigYaml.class).save();
        injector.getInstance(ChannelYaml.class).save();
        injector.getInstance(MessageYaml.class).save();
    }

    public void onReload() {
        // 重載配置檔
        injector.getInstance(ConfigYaml.class).load();
        injector.getInstance(ChannelYaml.class).load();
        injector.getInstance(MessageYaml.class).load();
        // 重載頻道配置檔
        injector.getInstance(ChannelManager.class)
                .getParser()
                .onReload(injector.getInstance(ChannelYaml.class));
    }

    /**
     * 從注入器取得實例
     *
     * @param clazz 類
     * @param <T>   類型
     * @return 實例
     */
    @NotNull
    public static <T> T getInstance(@NotNull Class<T> clazz) {
        return injector.getInstance(clazz);
    }

    /**
     * 指令
     *
     * @param sender  來源
     * @param command 指令
     * @param label   標籤
     * @param args    參數
     * @return 是否成功
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // 如果是玩家且沒有輸入參數，則開啟頻道選單
        if (sender instanceof Player player && args.length == 0) {
            MouBieCat.getInstance(ChannelMenu.class).open(player);
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
}
