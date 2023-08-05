package net.moubiecat.chatcontrol.channel;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IChannel {
    /**
     * 取得頻道前綴
     *
     * @return 頻道前綴
     */
    @NotNull String getPrefix();

    /**
     * 取得頻道名稱
     * 該名稱將作為對應 ChatControl 的通道名稱
     *
     * @return 頻道名稱
     */
    @NotNull String getChannelName();

    /**
     * 取得頻道權限
     *
     * @return 頻道權限
     */
    @Nullable Permission getPermission();

    /**
     * 判斷玩家是否有權限
     *
     * @param player 玩家
     * @return 是否有權限
     */
    boolean hasPermission(@NotNull Player player);

    /**
     * 取得頻道物品
     *
     * @param player 玩家
     * @return 頻道物品
     */
    @NotNull ItemStack buildChannelItem(@NotNull Player player, @NotNull NamespacedKey key);

    /**
     * 切換加入離開頻道
     *
     * @param player 玩家
     */
    void toggle(@NotNull Player player);

    /**
     * 玩家是否在頻道內
     *
     * @param player 玩家
     * @return 是否在頻道內
     */
    boolean isInChannel(@NotNull Player player);

    /**
     * 發送訊息到頻道
     *
     * @param player  玩家
     * @param message 訊息
     * @return 是否成功發送
     */
    boolean sendMessage(@NotNull Player player, @Nullable String message);
}
