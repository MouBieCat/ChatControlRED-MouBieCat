package net.moubiecat.chatcontrolred.menu;

import net.moubiecat.chatcontrolred.ItemService;
import net.moubiecat.chatcontrolred.MouBieCat;
import net.moubiecat.chatcontrolred.channel.ChannelItem;
import net.moubiecat.chatcontrolred.channel.ChannelPrefix;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.chatcontrol.model.Channel;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public final class ChannelMenu extends Menu {
    private final static int[] BORDER = {
            0, 1, 2, 3, 4, 5, 6, 7, 8,
            9, 17,
            18, 19, 20, 21, 22, 23, 24, 25, 26
    };
    private final static int CONTEXT_LENGTH = 7;
    private final static NamespacedKey CHANNEL_KEY = new NamespacedKey(MouBieCat.getInstance(), "channel");

    private final Collection<ChannelItem> channels = new LinkedList<>();

    /**
     * 建構子
     *
     * @param view     開啟選單的玩家
     * @param channels 頻道
     */
    public ChannelMenu(@NotNull Player view, Collection<ChannelPrefix> channels) {
        super(view, "        &8 ▼ 玩家頻道配置選單 ▼ ", (channels.size() / CONTEXT_LENGTH) + 1, MenuSize.THREE);
        this.channels.addAll(channels);
    }

    /**
     * 初始化選單內容
     */
    @Override
    protected void initialize() {
        // 清除所有內容
        this.clear();
        // 設置邊框
        this.drawBorder();
        // 繪製頻道
        this.drawChannels();
    }

    private void drawBorder() {
        final ItemStack itemStack = ItemService.build(Material.GRAY_STAINED_GLASS_PANE).name(" ").build().orElse(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        for (final int slot : ChannelMenu.BORDER)
            this.getInventory().setItem(slot, itemStack);
    }

    private void drawChannels() {
        // 如果為第二頁，則獲取第二頁的頻道
        this.channels.stream().skip((long) (this.page - 1) * CONTEXT_LENGTH).limit(CONTEXT_LENGTH).forEach(channelItem -> {
            final Channel channel = Channel.findChannel(channelItem.getChannel());
            final boolean inChannel = channel.isInChannel(this.view);
            final String display = channelItem.getDisplay();
            final List<String> lore = channelItem.getLore();

            ItemStack itemStack;
            if (inChannel)
                itemStack = ItemService.build(Material.BELL)
                        .name(display + " &2[已加入]")
                        .lore(lore)
                        .enchantment(Enchantment.DURABILITY, 3)
                        .flags(ItemFlag.HIDE_ENCHANTS)
                        .addPersistentDataContainer(CHANNEL_KEY, PersistentDataType.STRING, channelItem.getChannel())
                        .build()
                        .orElseThrow();

            else
                itemStack = ItemService.build(Material.BELL)
                        .name(display + " &c[未加入]")
                        .lore(lore)
                        .addPersistentDataContainer(CHANNEL_KEY, PersistentDataType.STRING, channelItem.getChannel())
                        .build()
                        .orElseThrow();
            this.getInventory().addItem(itemStack);
        });
    }

    @Nullable
    private String parsingChannel(@Nullable ItemStack itemStack) {
        if (itemStack == null)
            return "";

        final ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null)
            return "";

        return itemMeta.getPersistentDataContainer().get(CHANNEL_KEY, PersistentDataType.STRING);
    }

    /**
     * 選單開啟時觸發
     *
     * @param event 事件
     */
    @Override
    public void onOpen(@NotNull InventoryOpenEvent event) {
        // 不做任何事情
    }

    /**
     * 選單點選時觸發
     *
     * @param event 事件
     */
    @Override
    public void onClick(@NotNull InventoryClickEvent event) {
        event.setCancelled(true);

        final Player player = (Player) event.getWhoClicked();
        // 解析頻道名稱
        final String channelName = this.parsingChannel(event.getCurrentItem());
        // 如果頻道名稱為空，則不做任何事情
        if (channelName == null)
            return;

        // 獲取頻道
        final Channel channel = Channel.findChannel(channelName);
        // 如果玩家在頻道內，則離開頻道，否則加入頻道
        if (channel.isInChannel(player)) {
            channel.leavePlayer(player);
            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3.0F, 3.0F);
        } else {
            channel.joinPlayer(player, Channel.Mode.READ);
            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
        }

        // 重新開啟選單
        this.open(this.page);
    }

    /**
     * 選單關閉時觸發
     *
     * @param event 事件
     */
    @Override
    public void onClose(@NotNull InventoryCloseEvent event) {
        // 不做任何事情
    }
}
