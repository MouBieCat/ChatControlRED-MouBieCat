package net.moubiecat.chatcontrol.listener;

import com.google.inject.Inject;
import net.moubiecat.chatcontrol.injector.ChannelManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

public final class ChatListener implements Listener {
    private @Inject ChannelManager manager;

    /**
     * 玩家聊天事件
     *
     * @param event 玩家聊天事件
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerChatEvent(@NotNull AsyncPlayerChatEvent event) {
        // 發送訊息到指定頻道，如果返回 true 則取消事件。代表已經發送訊息到頻道
        event.setCancelled(this.manager.sendMessage(event.getPlayer(), event.getMessage()));
    }

}
