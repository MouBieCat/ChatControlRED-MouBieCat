package net.moubiecat.chatcontrolred.listener;

import net.moubiecat.chatcontrolred.MouBieCat;
import net.moubiecat.chatcontrolred.menu.ChannelMenu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;

public class CommandListener implements Listener {
    /**
     * PlayerCommandPreprocessEvent 監聽
     *
     * @param event PlayerCommandPreprocessEvent
     */
    @EventHandler
    public void onCommand(@NotNull PlayerCommandPreprocessEvent event) {
        final String message = event.getMessage();
        // 如果玩家輸入的指令是 /channel
        if (message.equalsIgnoreCase("/channel")) {
            // 幹掉 ChatControl 的指令調用
            event.setCancelled(true);

            // 開啟頻道選單
            new ChannelMenu(
                    event.getPlayer(),
                    MouBieCat.getInstance().getChannelManager().getChannels()).open();
        }
    }
}
