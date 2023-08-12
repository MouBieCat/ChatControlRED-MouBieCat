package net.moubiecat.chatcontrol.channel;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.chatcontrol.model.Channel;

import java.util.List;

final class MBDefaultChannel extends MBChannel {
    /**
     * 建構子
     *
     * @param channelName 頻道名稱
     * @param material    頻道物品
     * @param displayName 頻道顯示名稱
     * @param lore        頻道描述
     */
    public MBDefaultChannel(@NotNull String channelName, @NotNull Material material, @NotNull String displayName, @NotNull List<String> lore) {
        super("", channelName, null, material, displayName, lore);
    }

    /**
     * 加入頻道
     * 我們必須改寫 ChatControlRed 插件要求必須要有一個 WRITE 模式的頻道
     *
     * @param player 玩家
     */
    @Override
    protected boolean joinChannel(@NotNull Player player) {
        if (!this.isInChannel(player))
            return this.channel.joinPlayer(player, Channel.Mode.WRITE);
        return false;
    }

    @Override
    public boolean hasPermission(@NotNull Player player) {
        // 預設頻道不需要權限
        return true;
    }

    @Override
    public boolean sendMessage(@NotNull Player player, @Nullable String message) {
        // 交給 ChatControl 插件處理
        return false;
    }

    /**
     * 檢查訊息是否為頻道前綴
     *
     * @param message 訊息
     * @return 是否為頻道前綴
     */
    @Override
    protected boolean checkPrefix(@NotNull String message) {
        // 預設頻道不需要前綴
        return false;
    }
}
