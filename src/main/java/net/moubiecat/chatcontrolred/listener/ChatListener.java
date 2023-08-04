package net.moubiecat.chatcontrolred.listener;

import net.moubiecat.chatcontrolred.MouBieCat;
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
        // 判斷是否為預設頻道，如果不是我們要取消事件調用。
        if (MouBieCat.getInstance().getChannelManager().sendMessage(event.getPlayer(), event.getMessage()))
            // 幹掉 ChatControlRed 原本的發送訊息功能
            event.setCancelled(true);
    }
}
