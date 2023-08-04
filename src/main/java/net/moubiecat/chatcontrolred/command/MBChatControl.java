package net.moubiecat.chatcontrolred.command;

import net.moubiecat.chatcontrolred.MouBieCat;
import net.moubiecat.chatcontrolred.menu.ChannelMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public final class MBChatControl implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        // 開啟選單
        if (strings.length == 0) {
            ChannelMenu.openMenu(commandSender);
            return true;
        }

        // 重載插件
        if (strings[0].equalsIgnoreCase("reload") && commandSender.hasPermission("mbchatcontrol.admin")) {
            MouBieCat.getInstance().onReload();
            commandSender.sendMessage("§c系統 §7㇣ §f插件配置已重新加載。");
            return true;
        }

        return false;
    }
}
