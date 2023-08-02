package net.moubiecat.chatcontrolred.channel;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.chatcontrol.model.Channel;

import java.util.ArrayList;
import java.util.List;

public class ChannelPrefix implements ChannelItem {
    private final String prefix;
    private final String channel;
    private final String display;
    private final List<String> lore;

    /**
     * @param prefix  前缀
     * @param channel 频道
     */
    public ChannelPrefix(@NotNull String prefix, @NotNull String channel) {
        this(prefix, channel, channel, new ArrayList<>());
    }

    /**
     * @param prefix  前缀
     * @param channel 频道
     */
    public ChannelPrefix(@NotNull String prefix, @NotNull String channel, @NotNull String display, @NotNull List<String> lore) {
        this.prefix = prefix;
        this.channel = channel;
        this.display = display;
        this.lore = lore;
    }

    /**
     * 判斷頻道前綴是否相同
     *
     * @param message 訊息
     * @return 是否相同
     */
    protected boolean checkChannelPrefix(@Nullable String message) {
        if (message == null)
            return false;

        // 首先我們要知道當前的頻道前綴有多長
        final int channelPrefixLength = prefix.length();

        try {
            // 將相應長度的訊息截取出來，但是要注意長度裁切異常。
            final String messagePrefix = message.substring(0, channelPrefixLength);
            // 判斷是否相同
            return this.prefix.equals(messagePrefix);
        } catch (final IndexOutOfBoundsException ignored) {
            return false;
        }
    }

    /**
     * 發送訊息到頻道
     *
     * @param sender  發送者
     * @param message 訊息
     */
    public final boolean sendMessageToChannel(@NotNull Player sender, @Nullable String message) {
        if (message == null)
            return false;

        // 判斷是否為頻道前綴
        if (this.checkChannelPrefix(message)) {
            try {
                // 處理裁切的訊息
                final String finalMessage = message.substring(this.prefix.length());

                // 獲取頻道
                final Channel channel = Channel.findChannel(this.channel);
                // 判斷是否在頻道中
                if (channel.isInChannel(sender))
                    channel.sendMessage(sender, finalMessage, true);
                else
                    sender.sendMessage("§c系統 §7㇣ §f你沒有加入 §e" + this.channel + " §f因此無法發送訊息。");
                return true;
            } catch (final IndexOutOfBoundsException ignored) {
            }
        }

        return false;
    }

    /**
     * 獲取前綴
     *
     * @return 前綴
     */
    @NotNull
    public final String getPrefix() {
        return prefix;
    }

    /**
     * 獲取頻道名稱
     *
     * @return 頻道
     */
    @NotNull
    public final String getChannel() {
        return channel;
    }

    /**
     * 獲取頻道顯示名稱
     *
     * @return 名稱
     */
    @Override
    public final @NotNull String getDisplay() {
        return this.display;
    }

    /**
     * 獲取頻道描述
     *
     * @return 描述
     */
    @Override
    public final @NotNull List<String> getLore() {
        return this.lore;
    }
}
