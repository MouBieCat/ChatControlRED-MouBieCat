package net.moubiecat.chatcontrolred.channel;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.chatcontrol.model.Channel;

import java.util.List;

public final class DefaultChannelPrefix extends ChannelPrefix {
    /**
     * @param channel  频道
     * @param material 材質
     * @param display  顯示
     * @param lore     說明
     */
    public DefaultChannelPrefix(@NotNull String channel, @NotNull Material material, @NotNull String display, @NotNull List<String> lore) {
        super("", channel, material, display, lore);
    }

    /**
     * 判斷頻道前綴是否相同
     * 該方法已經被重寫，所以永遠返回 true
     *
     * @param message 訊息
     * @return 是否相同
     */
    @Override
    protected boolean checkChannelPrefix(@Nullable String message) {
        // 該方法已經被重寫，所以永遠返回 true
        return true;
    }

    /**
     * 發送訊息到頻道
     *
     * @param sender  發送者
     * @param message 訊息
     */
    @Override
    public boolean sendMessageToChannel(@NotNull Player sender, @Nullable String message) {
        // 預設頻道本身不處理發送訊息功能，所以永遠返回 true
        return true;
    }

    /**
     * 加入頻道
     *
     * @param player 玩家
     */
    @Override
    public void joinChannel(@NotNull Player player) {
        final org.mineacademy.chatcontrol.model.Channel channel = org.mineacademy.chatcontrol.model.Channel.findChannel(this.channel);
        channel.joinPlayer(player, Channel.Mode.WRITE);
    }
}
