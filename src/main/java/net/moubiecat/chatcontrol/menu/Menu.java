package net.moubiecat.chatcontrol.menu;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public abstract class Menu implements InventoryHolder, MenuHandler {
    protected final Inventory inventory;
    private int page = 1;

    /**
     * 建構子
     *
     * @param size  選單大小
     * @param title 選單標題
     */
    public Menu(@NotNull MenuSize size, @NotNull String title) {
        this.inventory = Bukkit.createInventory(this, size.getSize(), title);
    }

    /**
     * 開啟選單
     *
     * @param viewer 開啟選單的玩家
     */
    public final void open(@NotNull Player viewer) {
        viewer.openInventory(this.inventory);
        this.refresh(viewer);
    }

    /**
     * 重新整理選單
     *
     * @param viewer 開啟選單的玩家
     */
    public final void refresh(@NotNull Player viewer) {
        this.initialize(viewer, this.page);
    }

    /**
     * 下一頁
     *
     * @param viewer 開啟選單的玩家
     */
    public final void next(@NotNull Player viewer) {
        if (!this.hasNextPage(viewer, this.page))
            return;
        this.page++;
        viewer.playSound(viewer.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1.0f, 1.0f);
    }

    /**
     * 上一頁
     *
     * @param viewer 開啟選單的玩家
     */
    public final void previous(@NotNull Player viewer) {
        if (this.page <= 1)
            return;
        this.page--;
        viewer.playSound(viewer.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1.0f, 1.0f);
    }

    /**
     * 初始化選單內容
     *
     * @param view 開啟選單的玩家
     * @param page 頁數
     */
    protected abstract void initialize(@NotNull Player view, int page);

    /**
     * 是否有下一頁
     *
     * @param player 開啟選單的玩家
     * @param page   頁數
     * @return 是否有下一頁
     */
    protected boolean hasNextPage(@NotNull Player player, int page) {
        return false;
    }

    /**
     * 取得選單實例
     *
     * @return 選單實例
     */
    @Override
    public final @NotNull Inventory getInventory() {
        return this.inventory;
    }

    /**
     * InventoryOpenEvent 處理
     *
     * @param event 事件
     */
    @Override
    public void onInventoryOpen(@NotNull InventoryOpenEvent event) {
    }

    /**
     * InventoryClickEvent 處理
     *
     * @param event 事件
     */
    @Override
    public void onInventoryClick(@NotNull InventoryClickEvent event) {
    }

    /**
     * InventoryCloseEvent 處理
     *
     * @param event 事件
     */
    @Override
    public void onInventoryClose(@NotNull InventoryCloseEvent event) {
    }
}
