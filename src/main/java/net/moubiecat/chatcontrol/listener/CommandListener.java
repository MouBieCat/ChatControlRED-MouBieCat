package net.moubiecat.chatcontrol.listener;

import com.google.inject.Inject;
import net.moubiecat.chatcontrol.MouBieCat;
import net.moubiecat.chatcontrol.menu.ChannelMenu;
import net.moubiecat.chatcontrol.settings.ConfigYaml;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;

public final class CommandListener implements Listener {
    private @Inject ConfigYaml configYaml;

    /**
     * 玩家輸入指令事件
     *
     * @param event 事件
     */
    @EventHandler
    public void onCommand(@NotNull PlayerCommandPreprocessEvent event) {
        if (configYaml.getCommands().contains(event.getMessage().toLowerCase())) {
            MouBieCat.getInstance(ChannelMenu.class).open(event.getPlayer());
            event.setCancelled(true);
        }
    }
}
