package net.moubiecat.chatcontrolred.listener;

import net.moubiecat.chatcontrolred.menu.ChannelMenu;
import net.moubiecat.chatcontrolred.setting.ConfigYaml;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandListener implements Listener {
    private final List<String> commandList = new ArrayList<>();

    /**
     * 建構子
     *
     * @param config 配置檔
     */
    public CommandListener(@NotNull ConfigYaml config) {
        this.commandList.addAll(config.getCommandAliases());
    }

    /**
     * PlayerCommandPreprocessEvent 監聽
     *
     * @param event PlayerCommandPreprocessEvent
     */
    @EventHandler
    public void onCommand(@NotNull PlayerCommandPreprocessEvent event) {
        final String message = event.getMessage();

        // 如果玩家輸入的指令是 commandList 裡面的指令
        if (this.commandList.contains(message.toLowerCase())) {
            // 幹掉 ChatControl 的指令調用
            event.setCancelled(true);

            // 開啟頻道選單
            ChannelMenu.openMenu(event.getPlayer());
        }
    }
}
