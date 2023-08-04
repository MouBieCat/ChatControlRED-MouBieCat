package net.moubiecat.chatcontrolred.channel;

import net.moubiecat.chatcontrolred.menu.Button;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface Channel extends Button {
    /**
     * 獲取前綴
     *
     * @return 前綴
     */
    @NotNull String getPrefix();

    /**
     * 獲取頻道名稱
     *
     * @return 名稱
     */
    @NotNull String getChannel();

    /**
     * 加入或離開頻道，根據玩家目前的狀態
     *
     * @param player 玩家
     */
    void joinOrLeaveChannel(@NotNull Player player);

    /**
     * 玩家是否在頻道中
     *
     * @param player 玩家
     * @return 是否在頻道
     */
    boolean isInChannel(@NotNull Player player);
}
