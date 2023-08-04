package net.moubiecat.chatcontrolred.channel;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ChannelPrefix implements Channel {
    protected final String prefix;
    protected final String channel;

    private final Material material;
    private final String display;
    private final List<String> lore;

    /**
     * @param prefix   前缀
     * @param channel  频道
     * @param material 材質
     * @param display  顯示
     * @param lore     說明
     */
    public ChannelPrefix(@NotNull String prefix, @NotNull String channel, @NotNull Material material, @NotNull String display, @NotNull List<String> lore) {
        this.prefix = prefix;
        this.channel = channel;
        this.material = material;
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
    public boolean sendMessageToChannel(@NotNull Player sender, @Nullable String message) {
        if (message == null)
            return false;

        // 判斷是否為頻道前綴
        if (this.checkChannelPrefix(message)) {
            try {
                // 處理裁切的訊息
                final String finalMessage = message.substring(this.prefix.length());
                // 判斷是否為空
                if (finalMessage.isEmpty())
                    return true;

                // 獲取頻道
                final org.mineacademy.chatcontrol.model.Channel channel = org.mineacademy.chatcontrol.model.Channel.findChannel(this.channel);
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
     * 加入頻道
     *
     * @param player 玩家
     */
    public void joinChannel(@NotNull Player player) {
        final org.mineacademy.chatcontrol.model.Channel channel = org.mineacademy.chatcontrol.model.Channel.findChannel(this.channel);
        channel.joinPlayer(player, org.mineacademy.chatcontrol.model.Channel.Mode.READ);
    }

    /**
     * 離開頻道
     *
     * @param player 玩家
     */
    public void leaveChannel(@NotNull Player player) {
        final org.mineacademy.chatcontrol.model.Channel channel = org.mineacademy.chatcontrol.model.Channel.findChannel(this.channel);
        channel.leavePlayer(player);
    }

    /**
     * 加入或離開頻道
     *
     * @param player 玩家
     */
    @Override
    public final void joinOrLeaveChannel(@NotNull Player player) {
        final org.mineacademy.chatcontrol.model.Channel channel = org.mineacademy.chatcontrol.model.Channel.findChannel(this.channel);
        if (channel.isInChannel(player))
            this.leaveChannel(player);

        else
            this.joinChannel(player);
    }

    /**
     * 玩家是否在頻道中
     *
     * @param player 玩家
     * @return 是否在頻道
     */
    @Override
    public boolean isInChannel(@NotNull Player player) {
        final org.mineacademy.chatcontrol.model.Channel channel = org.mineacademy.chatcontrol.model.Channel.findChannel(this.channel);
        return channel.isInChannel(player);
    }

    @NotNull
    @Override
    public final Material getMaterial() {
        return this.material;
    }

    /**
     * 獲取頻道顯示名稱
     *
     * @return 名稱
     */
    @Override
    @NotNull
    public final String getDisplay() {
        return this.display;
    }

    /**
     * 獲取頻道描述
     *
     * @return 描述
     */
    @Override
    @NotNull
    public final List<String> getLore() {
        return this.lore;
    }
}
