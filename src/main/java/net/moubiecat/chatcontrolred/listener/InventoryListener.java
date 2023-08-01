package net.moubiecat.chatcontrolred.listener;

import net.moubiecat.chatcontrolred.menu.MenuHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public final class InventoryListener implements Listener {
    /**
     * 選單開啟時觸發
     *
     * @param event 事件
     */
    @EventHandler
    public void onInventoryOpen(@NotNull InventoryOpenEvent event) {
        final InventoryHolder holder = event.getInventory().getHolder();

        if (holder instanceof MenuHandler) {
            event.setCancelled(((MenuHandler) holder).onOpen(event));
        }
    }

    /**
     * 選單點選時觸發
     *
     * @param event 事件
     */
    @EventHandler
    public void onInventoryClick(@NotNull InventoryClickEvent event) {
        final InventoryHolder holder = event.getInventory().getHolder();

        if (holder instanceof MenuHandler) {
            event.setCancelled(((MenuHandler) holder).onClick(event));
        }
    }

    /**
     * 選單關閉時觸發
     *
     * @param event 事件
     */
    @EventHandler
    public void onInventoryClose(@NotNull InventoryCloseEvent event) {
        final InventoryHolder holder = event.getInventory().getHolder();

        if (holder instanceof MenuHandler) {
            ((MenuHandler) holder).onClose(event);
        }
    }
}
