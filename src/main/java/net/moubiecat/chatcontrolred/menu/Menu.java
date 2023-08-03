package net.moubiecat.chatcontrolred.menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public abstract class Menu implements InventoryHolder, MenuHandler {
    protected final Inventory inventory;
    protected final Player view;
    protected final MenuSize size;

    protected final int max;
    protected int page = 1;

    /**
     * 建構子
     *
     * @param view  開啟選單的玩家
     * @param title 選單標題
     * @param max   最大頁數
     * @param size  選單大小
     */
    public Menu(@NotNull Player view, @NotNull String title, int max, MenuSize size) {
        this.view = view;
        this.max = max;
        this.size = size;
        this.inventory = Bukkit.createInventory(this, this.size.getSize(), ChatColor.translateAlternateColorCodes('&', title));
    }

    /**
     * 開啟選單
     */
    public final void open() {
        if (this.page > this.max)
            this.page = this.max;

        else if (this.page < 1)
            this.page = 1;

        this.initialize();
        this.view.openInventory(this.inventory);
    }

    /**
     * 下一頁
     */
    public void next() {
        this.page++;
        this.open();
    }

    /**
     * 上一頁
     */
    public void previous() {
        this.page--;
        this.open();
    }

    /**
     * 初始化選單內容
     */
    protected abstract void initialize();

    /**
     * 清除選單內容
     */
    protected final void clear() {
        this.inventory.clear();
    }

    /**
     * 取得選單實例
     *
     * @return 選單實例
     */
    @NotNull
    @Override
    public final Inventory getInventory() {
        return this.inventory;
    }

    /**
     * InventoryOpenEvent 處理
     *
     * @param event 事件
     */
    @Override
    public void onOpen(@NotNull InventoryOpenEvent event) {
    }

    /**
     * InventoryClickEvent 處理
     *
     * @param event 事件
     */
    @Override
    public void onClick(@NotNull InventoryClickEvent event) {
    }

    /**
     * InventoryCloseEvent 處理
     *
     * @param event 事件
     */
    @Override
    public void onClose(@NotNull InventoryCloseEvent event) {
    }
}
