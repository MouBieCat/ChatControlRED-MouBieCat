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
    protected final MenuSize size;

    protected final int max;
    private int page = 1;

    /**
     * 建構子
     *
     * @param title 選單標題
     * @param max   最大頁數
     * @param size  選單大小
     */
    public Menu(@NotNull String title, int max, MenuSize size) {
        this.max = max;
        this.size = size;
        this.inventory = Bukkit.createInventory(this, this.size.getSize(), title);
    }

    /**
     * 檢查頁數是否合理
     * 若不合理則將頁數設置為最大或最小值
     */
    private void checkPage() {
        // 檢查頁數是否合理
        if (this.page > this.max)
            this.page = this.max;
        else if (this.page < 1)
            this.page = 1;
    }

    /**
     * 開啟選單
     *
     * @param viewer 開啟選單的玩家
     */
    public final void open(@NotNull Player viewer) {
        this.checkPage();
        viewer.openInventory(this.inventory);
        this.initialize(viewer, this.page);
    }

    /**
     * 重新整理選單
     *
     * @param viewer 開啟選單的玩家
     */
    public final void refresh(@NotNull Player viewer) {
        this.checkPage();
        this.initialize(viewer, this.page);
    }

    /**
     * 下一頁
     *
     * @param viewer 開啟選單的玩家
     */
    public final void next(@NotNull Player viewer) {
        this.page++;
        this.refresh(viewer);
        this.inventory.getViewers().stream()
                .filter(humanEntity -> humanEntity instanceof Player)
                .forEach(humanEntity -> ((Player) humanEntity).playSound(humanEntity, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5.0f, 5.0f));
    }

    /**
     * 上一頁
     *
     * @param viewer 開啟選單的玩家
     */
    public final void previous(@NotNull Player viewer) {
        this.page--;
        this.refresh(viewer);
        this.inventory.getViewers().stream()
                .filter(humanEntity -> humanEntity instanceof Player)
                .forEach(humanEntity -> ((Player) humanEntity).playSound(humanEntity, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f));
    }

    /**
     * 初始化選單內容
     *
     * @param page 頁數
     */
    protected abstract void initialize(@NotNull Player view, int page);

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
