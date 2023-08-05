package net.moubiecat.chatcontrol.listener;

import com.google.inject.Inject;
import net.moubiecat.chatcontrol.injector.ConfigYaml;
import net.moubiecat.chatcontrol.menu.ChannelMenu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;

public final class CommandListener implements Listener {
    @Inject
    private ConfigYaml configYaml;

    /**
     * 玩家輸入指令事件
     *
     * @param event 事件
     */
    @EventHandler
    public void onCommand(@NotNull PlayerCommandPreprocessEvent event) {
        if (configYaml.getCommands().contains(event.getMessage().toLowerCase())) {
            ChannelMenu.open(event.getPlayer());
            event.setCancelled(true);
        }
    }
}
