package net.moubiecat.chatcontrol;

import net.moubiecat.chatcontrol.utils.Yaml;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;

public final class MessageYaml extends Yaml {
    public enum Message {
        YOU_NOT_IN_CHANNEL("YouNotInChannel", "§fYou are not in the {0} channel."),
        YOU_JOIN_CHANNEL("YouJoinChannel", "§fYou have joined the§e {0} §fchannel."),
        YOU_LEAVE_CHANNEL("YouLeaveChannel", "§fYou have left the§e {0} §fchannel."),
        ;

        private final String path;
        private final String def;

        Message(@NotNull String path, @NotNull String def) {
            this.path = path;
            this.def = def;
        }

        @NotNull
        public final String getPath() {
            return this.path;
        }

        @NotNull
        public final String getDef() {
            return this.def;
        }
    }

    /**
     * 建構子
     *
     * @param plugin 插件
     */
    public MessageYaml(@NotNull Plugin plugin) {
        super(plugin, "message.yml");
    }

    @NotNull
    public String getMessage(@NotNull Message message) {
        return this.configuration.getString(message.getPath(), message.getDef());
    }

    /**
     * 從配置檔發送訊息
     *
     * @param player  玩家
     * @param message 訊息
     */
    public void sendFormatMessage(@NotNull Player player, @NotNull MessageYaml.Message message, @NotNull Object... args) {
        final MessageFormat format = new MessageFormat(this.getMessage(message));
        player.sendMessage(format.format(args));
    }
}
