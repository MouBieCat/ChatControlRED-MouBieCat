package net.moubiecat.chatcontrolred.listener;

import net.moubiecat.chatcontrolred.MouBieCat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

public final class ChatListener implements Listener {
    /**
     * AsyncPlayerChatEvent 监听
     *
     * @param event AsyncPlayerChatEvent
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onChatControlChatEvent(@NotNull AsyncPlayerChatEvent event) {
        // 幹掉 ChatControlRed 原本的發送訊息功能
        event.setCancelled(true);

        // 自己處理發送訊息功能
        final Player player = event.getPlayer();
        final String message = event.getMessage();

        // 發送訊息
        MouBieCat.getInstance().getChannelManager().sendMessage(player, message);
    }
}
