package net.moubiecat.chatcontrol.listener;

import net.moubiecat.chatcontrol.ConfigYaml;
import net.moubiecat.chatcontrol.menu.ChannelMenu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class CommandListener implements Listener {
    private final List<String> commands = new ArrayList<>();

    /**
     * 构造函数
     *
     * @param configYaml 配置檔實例
     */
    public CommandListener(@NotNull ConfigYaml configYaml) {
        this.commands.addAll(configYaml.getCommands());
    }

    /**
     * 玩家輸入指令事件
     *
     * @param event 事件
     */
    @EventHandler
    public void onCommand(@NotNull PlayerCommandPreprocessEvent event) {
        if (commands.contains(event.getMessage().toLowerCase())) {
            ChannelMenu.open(event.getPlayer());
            event.setCancelled(true);
        }
    }
}
