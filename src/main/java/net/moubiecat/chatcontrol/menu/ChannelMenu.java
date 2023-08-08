package net.moubiecat.chatcontrol.menu;

import com.google.inject.Inject;
import net.moubiecat.chatcontrol.MouBieCat;
import net.moubiecat.chatcontrol.channel.ChannelManager;
import net.moubiecat.chatcontrol.services.ItemService;
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
    private final static int[] BORDER_SLOT = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26};
    private final static int PREVIOUS_PAGE_SLOT = 9;
    private final static int NEXT_PAGE_SLOT = 17;
    private final static int CHANNEL_BUTTON_SLOTS = 7;

    private final static NamespacedKey ACTION_KEY = new NamespacedKey(MouBieCat.getInstance(MouBieCat.class), "action");

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

    private final ChannelManager manager;

    /**
     * 建構子
     *
     * @param manager 頻道管理器
     */
    @Inject
    public ChannelMenu(@NotNull ChannelManager manager) {
        super("        §8 ▼ 玩家頻道配置選單 ▼ ", manager.getChannels().size() / CHANNEL_BUTTON_SLOTS + 1, MenuSize.THREE);
        this.manager = manager;
    }

    /**
     * 初始化選單內容
     *
     * @param page 頁數
     */
    @Override
    protected void initialize(@NotNull Player view, int page) {
        this.inventory.clear();
        // 設置選單邊框
        Arrays.stream(ChannelMenu.BORDER_SLOT).forEach(slot -> this.inventory.setItem(slot, this.borderItem));
        // 設置翻頁按鈕
        if (page > 1)
            this.inventory.setItem(PREVIOUS_PAGE_SLOT, this.previousItem);
        if (page < this.max)
            this.inventory.setItem(NEXT_PAGE_SLOT, this.nextItem);
        // 設置頻道按鈕
        this.manager.getChannels().stream()
                .skip((long) (page - 1) * CHANNEL_BUTTON_SLOTS)
                .limit(CHANNEL_BUTTON_SLOTS)
                .forEach(channel -> this.inventory.addItem(channel.buildChannelItem(view, ACTION_KEY)));
    }

    /**
     * InventoryClickEvent 處理
     *
     * @param event 事件
     */
    @Override
    public void onClick(@NotNull InventoryClickEvent event) {
        // 取消點擊事件
        event.setCancelled(true);

        // 獲取點擊的物品
        final Player player = (Player) event.getWhoClicked();
        final ItemStack currentItem = event.getCurrentItem();
        if (currentItem == null || currentItem.getItemMeta() == null)
            return;
        // 獲取物品的 Action
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
