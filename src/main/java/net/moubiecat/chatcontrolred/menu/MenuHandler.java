package net.moubiecat.chatcontrolred.menu;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;

public interface MenuHandler {
    /**
     * 選單開啟時觸發
     *
     * @param event 事件
     */
    void onOpen(@NotNull InventoryOpenEvent event);

    /**
     * 選單點選時觸發
     *
     * @param event 事件
     */
    void onClick(@NotNull InventoryClickEvent event);

    /**
     * 選單關閉時觸發
     *
     * @param event 事件
     */
    void onClose(@NotNull InventoryCloseEvent event);
}
