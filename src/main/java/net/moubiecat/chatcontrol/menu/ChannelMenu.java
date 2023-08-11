package net.moubiecat.chatcontrol.menu;

import com.google.inject.Inject;
import net.moubiecat.chatcontrol.MouBieCat;
import net.moubiecat.chatcontrol.channel.ChannelManager;
import net.moubiecat.chatcontrol.services.ItemService;
import net.moubiecat.chatcontrol.settings.ChannelYaml;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public final class ChannelMenu extends Menu {
    public final static NamespacedKey ACTION_KEY = new NamespacedKey(MouBieCat.getPlugin(), "action");

    private final static int[] BORDER_SLOT = {0, 1, 2, 3, 4, 5, 6, 7, 8, 18, 19, 20, 21, 22, 23, 24, 25, 26};
    private final static int[] CHANNEL_BUTTON_SLOTS = {10, 11, 12, 13, 14, 15, 16};
    private final static int PREVIOUS_PAGE_SLOT = 9;
    private final static int NEXT_PAGE_SLOT = 17;

    private final ItemStack borderItem = ItemService.build(Material.BLACK_STAINED_GLASS_PANE)
            .name(" ")
            .build()
            .orElseThrow();
    private final ItemStack previousItem = ItemService.build(Material.ARROW)
            .name("§f<-")
            .addPersistentDataContainer(ACTION_KEY, PersistentDataType.STRING, "previous")
            .build()
            .orElseThrow();
    private final ItemStack nextItem = ItemService.build(Material.ARROW)
            .name("§f->")
            .addPersistentDataContainer(ACTION_KEY, PersistentDataType.STRING, "next")
            .build()
            .orElseThrow();

    private @Inject ChannelManager manager;

    /**
     * 建構子
     */
    @Inject
    public ChannelMenu(@NotNull ChannelYaml yaml) {
        super(MenuSize.THREE, yaml.getInventoryTitle());
    }

    @Override
    protected void initialize(@NotNull Player view, int page) {
        this.inventory.clear();
        // 設置選單邊框
        Arrays.stream(ChannelMenu.BORDER_SLOT).forEach(slot -> this.inventory.setItem(slot, this.borderItem));
        // 設置翻頁按鈕
        this.inventory.setItem(PREVIOUS_PAGE_SLOT, this.previousItem);
        this.inventory.setItem(NEXT_PAGE_SLOT, this.nextItem);
        // 設置頻道按鈕
        for (int index = 0; index < CHANNEL_BUTTON_SLOTS.length; index++) {
            final int slot = CHANNEL_BUTTON_SLOTS[index];
            final int channelIndex = (page - 1) * CHANNEL_BUTTON_SLOTS.length + index;
            if (channelIndex >= this.manager.getChannels().size())
                break;
            final var channel = this.manager.getChannels().get(channelIndex);
            this.inventory.setItem(slot, channel.buildChannelItem(view, ACTION_KEY));
        }
    }

    /**
     * 是否有下一頁
     *
     * @param player 開啟選單的玩家
     * @param page   頁數
     * @return 是否有下一頁
     */
    @Override
    protected boolean hasNextPage(@NotNull Player player, int page) {
        return (page * CHANNEL_BUTTON_SLOTS.length) < this.manager.getChannels().size();
    }

    /**
     * InventoryClickEvent 處理
     *
     * @param event 事件
     */
    @Override
    @SuppressWarnings("ConstantConditions")
    public void onInventoryClick(@NotNull InventoryClickEvent event) {
        event.setCancelled(true);
        final Player player = (Player) event.getWhoClicked();
        final ItemStack currentItem = event.getCurrentItem();
        final ItemMeta itemMeta = currentItem.getItemMeta();
        final String action = itemMeta.getPersistentDataContainer().get(ACTION_KEY, PersistentDataType.STRING);
        if (action == null)
            return;

        // 根據 Action 執行對應的動作
        switch (action) {
            case "previous" -> this.previous(player);
            case "next" -> this.next(player);
            default -> this.manager.getChannel(action).ifPresent(channel -> channel.toggle(player));
        }

        // 更新選單
        this.refresh(player);
    }
}
