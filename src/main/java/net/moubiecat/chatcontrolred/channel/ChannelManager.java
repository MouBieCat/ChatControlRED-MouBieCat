package net.moubiecat.chatcontrolred.channel;

import net.moubiecat.chatcontrolred.Mappable;
import net.moubiecat.chatcontrolred.MouBieCat;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public final class ChannelManager implements Mappable<FileConfiguration> {
    private final List<ChannelPrefix> channels = new LinkedList<>();

    /**
     * 發送訊息
     *
     * @param player  玩家
     * @param message 訊息
     */
    public void sendMessage(@NotNull Player player, @Nullable String message) {
        // 如果訊息為空，則不處理
        if (message == null)
            return;

        // 發送到指定頻道
        for (final ChannelPrefix channel : this.channels) {
            // 如果發送成功，則不再繼續
            if (channel.sendMessageToChannel(player, message)) {
                return;
            }
        }
    }

    @NotNull
    public Collection<ChannelPrefix> getChannels() {
        return this.channels;
    }

    @Override
    public void onLoad(@NotNull FileConfiguration file) {
        // 解析頻道列表
        this.parseChannelPrefix(file);
        this.printChannels();
    }

    @Override
    public void onReload(@NotNull FileConfiguration file) {
        // 清空頻道列表
        this.channels.clear();
        // 重新解析頻道列表
        this.parseChannelPrefix(file);
        this.printChannels();
    }

    @Override
    public void onSave(@NotNull FileConfiguration file) {
        // 沒有任何保存內容
    }

    /**
     * 獲取頻道列表
     *
     * @param file 配置文件
     */
    private void parseChannelPrefix(@NotNull FileConfiguration file) {
        final ConfigurationSection channelsSection = file.getConfigurationSection("Channels");

        // 處理 `Channels:` 內容
        if (channelsSection != null) {
            // 遍歷所有頻道前綴
            final Set<String> channelsPrefix = channelsSection.getKeys(false);

            // 遍歷所有頻道前綴
            for (final String prefix : channelsPrefix) {
                final String channelName = file.getString("Channels." + prefix);
                if (channelName == null)
                    continue;
                final String channelDisplay = file.getString(prefix + ".display");
                if (channelDisplay == null)
                    continue;
                final List<String> lore = file.getStringList(prefix + ".lore");

                this.channels.add(new ChannelPrefix(prefix, channelName, channelDisplay, lore));
            }
        }

        // 處理 `DefaultChannel:` 內容
        final String defaultChannel = file.getString("DefaultChannel");
        if (defaultChannel != null) {
            final String channelDisplay = file.getString("default.display");
            if (channelDisplay == null)
                return;
            final List<String> lore = file.getStringList("default.lore");

            this.channels.add(new DefaultChannelPrefix(defaultChannel, channelDisplay, lore));
        }
    }

    public void printChannels() {
        for (final ChannelPrefix prefix : this.channels) {
            if (prefix instanceof DefaultChannelPrefix)
                MouBieCat.getInstance().getLogger().info("Default" + " -> " + prefix.getChannel());
            else
                MouBieCat.getInstance().getLogger().info(prefix.getPrefix() + " -> " + prefix.getChannel());
        }
    }
}
