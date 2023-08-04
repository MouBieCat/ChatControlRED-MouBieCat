package net.moubiecat.chatcontrolred.menu;

import net.moubiecat.chatcontrolred.MouBieCat;
import net.moubiecat.chatcontrolred.channel.ChannelManager;
import net.moubiecat.chatcontrolred.service.ItemService;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public final class ChannelMenu extends Menu {
    private final static NamespacedKey ACTION_KEY = new NamespacedKey(MouBieCat.getInstance(), "action");

    private final static int[] BORDER_SLOT = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26};
    private final static int PREVIOUS_PAGE_SLOT = 9;
    private final static int NEXT_PAGE_SLOT = 17;
    private final static int CHANNEL_BUTTON_SLOTS = 7;

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
     * @param view    開啟選單的玩家
     * @param manager 頻道管理器
     */
    private ChannelMenu(@NotNull Player view, @NotNull ChannelManager manager) {
        super(view, "        §8 ▼ 玩家頻道配置選單 ▼ ",
                manager.getChannels().size() / CHANNEL_BUTTON_SLOTS + 1, MenuSize.THREE);
        this.manager = manager;
    }

    /**
     * 初始化選單內容
     */
    @Override
    protected void initialize() {
        // 清除所有內容
        this.clear();
        // 繪製外框
        this.drawBorderButtons();
        // 繪製翻頁按鈕
        this.drawPageButton();
        // 繪製頻道
        this.drawChannelButtons();
    }

    /**
     * 繪製選單邊框
     */
    private void drawBorderButtons() {
        Arrays.stream(ChannelMenu.BORDER_SLOT).forEach(slot -> this.inventory.setItem(slot, this.borderItem));
    }

    /**
     * 繪製翻頁按鈕
     */
    private void drawPageButton() {
        if (this.page > 1)
            this.inventory.setItem(PREVIOUS_PAGE_SLOT, this.previousItem);

        if (this.page < this.max)
            this.inventory.setItem(NEXT_PAGE_SLOT, this.nextItem);
    }

    /**
     * 繪製所有頻道按鈕
     */
    private void drawChannelButtons() {
        this.manager.getChannels().stream().skip(
                (long) (this.page - 1) * CHANNEL_BUTTON_SLOTS).limit(CHANNEL_BUTTON_SLOTS).forEach(channelPrefix -> {
            if (channelPrefix.isInChannel(this.view)) this.inventory.addItem(
                    ItemService.build(channelPrefix.getMaterial())
                            .name(channelPrefix.getDisplay() + " §2[已加入]§r")
                            .lore(channelPrefix.getLore())
                            .enchantment(Enchantment.DURABILITY, 3)
                            .flags(ItemFlag.HIDE_ENCHANTS)
                            .addPersistentDataContainer(ACTION_KEY, PersistentDataType.STRING, channelPrefix.getPrefix())
                            .build()
                            .orElseThrow());
            else this.inventory.addItem(
                    ItemService.build(channelPrefix.getMaterial())
                            .name(channelPrefix.getDisplay() + " §c[未加入]§r")
                            .lore(channelPrefix.getLore())
                            .addPersistentDataContainer(ACTION_KEY, PersistentDataType.STRING, channelPrefix.getPrefix())
                            .build()
                            .orElseThrow());
        });
    }

    /**
     * 選單點選時觸發
     *
     * @param event 事件
     */
    @Override
    public void onClick(@NotNull InventoryClickEvent event) {
        event.setCancelled(true);

        // 點擊的不是項目
        final Player player = (Player) event.getWhoClicked();
        final ItemStack currentItem = event.getCurrentItem();
        if (currentItem == null)
            return;

        // 判斷 ItemMeta
        final ItemMeta itemMeta = currentItem.getItemMeta();
        if (itemMeta == null)
            return;

        // 判斷是否有 Action
        final String action = itemMeta.getPersistentDataContainer().get(ACTION_KEY, PersistentDataType.STRING);
        if (action == null)
            return;

        // 判斷 Action
        switch (action) {
            // 點擊翻頁按鈕
            case "previous" -> {
                this.previous();
                player.playSound(player, Sound.ITEM_BOOK_PAGE_TURN, 1.0F, 1.0F);
            }
            // 點擊翻頁按鈕
            case "next" -> {
                this.next();
                player.playSound(player, Sound.ITEM_BOOK_PAGE_TURN, 1.0F, 1.0F);
            }
            // 點擊頻道按鈕
            default -> {
                MouBieCat.getInstance().getChannelManager().getChannel(action).joinOrLeaveChannel(player);
                player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                this.open();
            }
        }
    }

    /**
     * 開啟選單
     *
     * @param sender 命令發送者
     */
    public static void openMenu(@NotNull CommandSender sender) {
        if (sender instanceof Player player)
            new ChannelMenu(player, MouBieCat.getInstance().getChannelManager()).open();
    }
}
