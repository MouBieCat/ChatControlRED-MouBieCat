package net.moubiecat.chatcontrol.channel;

import jdk.jfr.Description;
import net.moubiecat.chatcontrol.MouBieCat;
import net.moubiecat.chatcontrol.injector.MessageYaml;
import net.moubiecat.chatcontrol.service.ItemService;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.chatcontrol.lib.exception.EventHandledException;
import org.mineacademy.chatcontrol.model.Channel;

import java.util.List;

public class MBChannel implements IChannel {
    private final String prefix;
    private final String channelName;

    private final Material material;
    private final String displayName;
    private final List<String> lore;

    @Description("對應 ChatControl 的通道")
    protected final Channel channel;

    /**
     * 建構子
     *
     * @param prefix      頻道前綴
     * @param channelName 頻道名稱
     * @param material    頻道物品
     * @param displayName 頻道顯示名稱
     * @param lore        頻道描述
     */
    public MBChannel(@NotNull String prefix, @NotNull String channelName,
                     @NotNull Material material, @NotNull String displayName, @NotNull List<String> lore) {
        // 初始化內部物件
        this.prefix = prefix;
        this.channelName = channelName;
        this.material = material;
        this.displayName = displayName;
        this.lore = lore;

        // 初始化外部物件
        this.channel = Channel.findChannel(this.channelName);
    }

    /**
     * 取得頻道前綴
     *
     * @return 頻道前綴
     */
    public final @NotNull String getPrefix() {
        return this.prefix;
    }

    /**
     * 取得頻道名稱
     * 該名稱將作為對應 ChatControl 的通道名稱
     *
     * @return 頻道名稱
     */
    public final @NotNull String getChannelName() {
        return this.channelName;
    }

    /**
     * 取得頻道物品
     *
     * @param player 玩家
     * @return 頻道物品
     */
    public final @NotNull ItemStack buildChannelItem(@NotNull Player player, @NotNull NamespacedKey key) {
        final boolean inChannel = this.channel.isInChannel(player);
        // 建立基礎頻道物品
        final ItemStack channelItem = ItemService.build(this.material)
                .name(this.displayName)
                .lore(this.lore)
                .flags(ItemFlag.HIDE_ENCHANTS)
                .addPersistentDataContainer(key, PersistentDataType.STRING, this.channelName)
                .build()
                .orElseThrow();
        // 如果玩家在頻道內，則將頻道物品附魔
        return inChannel ? ItemService.edit(channelItem).name(this.displayName + " §2[已加入]").enchantment(Enchantment.DURABILITY, 3).build().orElseThrow() :
                channelItem;
    }

    /**
     * 加入頻道
     *
     * @param player 玩家
     */
    protected boolean joinChannel(@NotNull Player player) {
        return this.channel.joinPlayer(player, Channel.Mode.READ);
    }

    /**
     * 離開頻道
     *
     * @param player 玩家
     */
    protected boolean leaveChannel(@NotNull Player player) {
        return this.channel.leavePlayer(player);
    }

    /**
     * 切換加入離開頻道
     *
     * @param player 玩家
     */
    public final void toggle(@NotNull Player player) {
        if (this.isInChannel(player) && this.leaveChannel(player)) {
            // 如果玩家在頻道內且離開頻道成功，則發送離開頻道訊息
            MouBieCat.getInjector()
                    .getInstance(MessageYaml.class)
                    .sendFormatMessage(player, MessageYaml.Message.YOU_LEAVE_CHANNEL, this.channelName);
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5, 5);
        } else if (this.joinChannel(player)) {
            // 如果玩家不在頻道內且加入頻道成功，則發送加入頻道訊息
            MouBieCat.getInjector()
                    .getInstance(MessageYaml.class)
                    .sendFormatMessage(player, MessageYaml.Message.YOU_JOIN_CHANNEL, this.channelName);
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        }
    }

    /**
     * 玩家是否在頻道內
     *
     * @param player 玩家
     * @return 是否在頻道內
     */
    public final boolean isInChannel(@NotNull Player player) {
        return this.channel.isInChannel(player);
    }

    /**
     * 發送訊息到頻道
     *
     * @param player  玩家
     * @param message 訊息
     * @return 是否成功發送
     */
    public boolean sendMessage(@NotNull Player player, @Nullable String message) {
        // 如果訊息為空或為空字串或為頻道前綴，則不發送訊息
        if (message == null || message.isEmpty() || !this.checkPrefix(message))
            return false;

        try {
            // 檢查訊息是否為頻道前綴
            final String finalMessage = message.substring(this.prefix.length());
            if (finalMessage.isEmpty())
                return true;

            // 發送訊息
            if (this.isInChannel(player))
                // 如果玩家在頻道內，則發送訊息到頻道
                this.channel.sendMessage(player, finalMessage, true);
            else
                // 如果玩家不在頻道內，則發送訊息給玩家
                MouBieCat.getInjector()
                        .getInstance(MessageYaml.class)
                        .sendFormatMessage(player, MessageYaml.Message.YOU_NOT_IN_CHANNEL, this.channelName);
            return true;
        } catch (final IndexOutOfBoundsException | EventHandledException ignored) {
            return false;
        }
    }

    /**
     * 檢查訊息是否為頻道前綴
     *
     * @param message 訊息
     * @return 是否為頻道前綴
     */
    protected boolean checkPrefix(@NotNull String message) {
        try {
            // 檢查訊息是否為頻道前綴
            final String substring = message.substring(0, this.prefix.length());
            return substring.equals(this.prefix);
        } catch (final IndexOutOfBoundsException ignored) {
            return false;
        }
    }
}
