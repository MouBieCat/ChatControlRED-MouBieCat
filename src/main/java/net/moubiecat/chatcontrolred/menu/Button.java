package net.moubiecat.chatcontrolred.menu;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Button {
    /**
     * 獲取物品材質
     *
     * @return 材質
     */
    default @NotNull Material getMaterial() {
        return Material.AIR;
    }

    /**
     * 獲取物品顯示名稱
     *
     * @return 名稱
     */
    @NotNull String getDisplay();

    /**
     * 獲取物品描述
     *
     * @return 描述
     */
    @NotNull List<String> getLore();
}
