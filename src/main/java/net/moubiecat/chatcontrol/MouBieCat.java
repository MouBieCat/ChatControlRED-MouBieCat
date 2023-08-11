package net.moubiecat.chatcontrol;

import net.moubiecat.chatcontrol.channel.ChannelManager;
import net.moubiecat.chatcontrol.database.Database;
import net.moubiecat.chatcontrol.database.PlayerDatabase;
import net.moubiecat.chatcontrol.database.UUIDHandler;
import net.moubiecat.chatcontrol.listener.CommandListener;
import net.moubiecat.chatcontrol.listener.InventoryListener;
import net.moubiecat.chatcontrol.listener.PlayerListener;
import net.moubiecat.chatcontrol.settings.ChannelYaml;
import net.moubiecat.chatcontrol.settings.ConfigYaml;
import net.moubiecat.chatcontrol.settings.MessageYaml;
import org.apache.ibatis.session.SqlSessionFactory;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class MouBieCat extends JavaPlugin {
    private static final InjectRegistration INJECT_REGISTRATION = new InjectRegistration();
    private static final DatabaseRegistration DATABASE_REGISTRATION = new DatabaseRegistration();

    @Override
    public void onEnable() {
        // 資料庫依賴
        DATABASE_REGISTRATION.registerTypeHandler(UUID.class, UUIDHandler.class);
        DATABASE_REGISTRATION.registerMapper(Database.class);
        DATABASE_REGISTRATION.buildSqlSessionFactory();
        // 其它注入依賴
        INJECT_REGISTRATION.bindPluginInstance(MouBieCat.class, this);
        INJECT_REGISTRATION.bindInstance(ConfigYaml.class, new ConfigYaml(this));
        INJECT_REGISTRATION.bindInstance(ChannelYaml.class, new ChannelYaml(this));
        INJECT_REGISTRATION.bindInstance(MessageYaml.class, new MessageYaml(this));
        INJECT_REGISTRATION.bindInstance(ChannelManager.class, new ChannelManager());
        INJECT_REGISTRATION.bindInstance(Database.class, new PlayerDatabase());
        INJECT_REGISTRATION.bindInjector();

        // 創建 MySQL 資料庫
        InjectRegistration.INJECTOR.getInstance(Database.class).createTable();
        // 加載頻道配置檔
        InjectRegistration.INJECTOR.getInstance(ChannelManager.class)
                .getParser()
                .onLoad(InjectRegistration.INJECTOR.getInstance(ChannelYaml.class));
        // 註冊事件
        Bukkit.getPluginManager().registerEvents(InjectRegistration.INJECTOR.getInstance(InventoryListener.class), this);
        Bukkit.getPluginManager().registerEvents(InjectRegistration.INJECTOR.getInstance(PlayerListener.class), this);
        Bukkit.getPluginManager().registerEvents(InjectRegistration.INJECTOR.getInstance(CommandListener.class), this);
    }

    @Override
    public void onDisable() {
        // 保存頻道配置檔
        InjectRegistration.INJECTOR.getInstance(ChannelManager.class)
                .getParser()
                .onSave(InjectRegistration.INJECTOR.getInstance(ChannelYaml.class));
        // 保存配置檔
        InjectRegistration.INJECTOR.getInstance(ConfigYaml.class).save();
        InjectRegistration.INJECTOR.getInstance(ChannelYaml.class).save();
        InjectRegistration.INJECTOR.getInstance(MessageYaml.class).save();
    }

    public void onReload() {
        // 重載配置檔
        InjectRegistration.INJECTOR.getInstance(ConfigYaml.class).load();
        InjectRegistration.INJECTOR.getInstance(ChannelYaml.class).load();
        InjectRegistration.INJECTOR.getInstance(MessageYaml.class).load();
        // 重載頻道配置檔
        InjectRegistration.INJECTOR.getInstance(ChannelManager.class)
                .getParser()
                .onReload(InjectRegistration.INJECTOR.getInstance(ChannelYaml.class));
    }

    /**
     * 從注入器取得實例
     *
     * @param clazz 類
     * @param <T>   類型
     * @return 實例
     */
    public static <T> @NotNull T getInstance(@NotNull Class<T> clazz) {
        return InjectRegistration.INJECTOR.getInstance(clazz);
    }

    /**
     * 取得 SqlSessionFactory
     *
     * @return SqlSessionFactory
     */
    public static @NotNull SqlSessionFactory getSqlSessionFactory() {
        return DatabaseRegistration.SQL_SESSION_FACTORY;
    }

    public static @NotNull MouBieCat getPlugin() {
        return getPlugin(MouBieCat.class);
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
        if (args.length == 1 && args[0].equalsIgnoreCase("reload") && sender.hasPermission("MBChatControl.reload")) {
            // 如果輸入參數為 reload，則重載插件
            this.onReload();
            sender.sendMessage("§6The plugin ChatControl-MouBieCat has been reloaded.");
            return true;
        }
        return false;
    }
}
