package net.moubiecat.chatcontrol.listener;

import com.google.inject.Inject;
import net.moubiecat.chatcontrol.MouBieCat;
import net.moubiecat.chatcontrol.channel.ChannelManager;
import net.moubiecat.chatcontrol.database.Data;
import net.moubiecat.chatcontrol.database.Database;
import net.moubiecat.chatcontrol.menu.ChannelMenu;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PlayerListener implements Listener {
    private @Inject MouBieCat plugin;
    private @Inject ChannelManager manager;
    private @Inject Database database;
    private final Map<UUID, Data> playerFirstChatData = new HashMap<>();

    /**
     * 玩家加入伺服器事件
     *
     * @param event 事件
     */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final Data data = database.selectData(player.getUniqueId());
        if (data == null) {
            // 插入資料庫
            database.insertData(new Data(player.getUniqueId()));
            playerFirstChatData.put(player.getUniqueId(), database.selectData(player.getUniqueId()));
        } else
            // 讀取資料庫，並插入快取
            playerFirstChatData.put(player.getUniqueId(), data);
    }

    /**
     * 玩家離開伺服器事件
     *
     * @param event 事件
     */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(@NotNull PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        // 更新資料庫
        database.updateData(playerFirstChatData.get(player.getUniqueId()));
        // 移除快取
        playerFirstChatData.remove(player.getUniqueId());
    }

    /**
     * 玩家聊天事件
     *
     * @param event 玩家聊天事件
     */
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerChatEvent(@NotNull AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();

        // 獲取玩家快取資料
        final Data data = playerFirstChatData.get(player.getUniqueId());
        // 如果是第一次發言
        if (data.isFirst()) {
            event.setCancelled(true);
            // 到同步執行緒執行
            Bukkit.getScheduler().runTask(this.plugin, () -> {
                // 顯示頻道選單
                MouBieCat.getInstance(ChannelMenu.class).open(player);
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                // 更新資料庫
                data.setNoFirst();
            });
            return;
        }

        // 發送訊息到指定頻道，如果返回 true 則取消事件。代表已經發送訊息到頻道
        event.setCancelled(this.manager.sendMessage(event.getPlayer(), event.getMessage()));
    }
}
