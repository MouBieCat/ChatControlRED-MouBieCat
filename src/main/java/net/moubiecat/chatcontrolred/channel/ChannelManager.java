package net.moubiecat.chatcontrolred.channel;

import net.moubiecat.chatcontrolred.Mappable;
import net.moubiecat.chatcontrolred.MouBieCat;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

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

        // 如果沒有，則發送到預設頻道
        final ChannelPrefix defaultChannel = this.getDefaultChannel();
        defaultChannel.sendMessageToChannel(player, message);
    }

    /**
     * 獲取頻道
     *
     * @param prefix 頻道前綴
     * @return 頻道前綴
     */
    @Nullable
    public ChannelPrefix getChannel(@NotNull String prefix) {
        return channels.stream()
                .filter(channel -> channel.getPrefix().equals(prefix))
                .findFirst()
                .orElse(null);
    }

    /**
     * 獲取預設頻道
     *
     * @return 頻道前綴
     */
    @NotNull
    public ChannelPrefix getDefaultChannel() {
        return channels.stream()
                .filter(channel -> channel instanceof DefaultChannelPrefix)
                .findFirst().orElseThrow();
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
        // 首先處理 `Channels:` 內容
        final ConfigurationSection channelsSection = file.getConfigurationSection("Channels");
        if (channelsSection != null) {
            // 遍歷所有頻道前綴
            for (final String prefix : channelsSection.getKeys(false)) {
                // 獲取頻道名稱
                final String channel = file.getString("Channels." + prefix);
                if (channel != null) {
                    // 創建頻道前綴
                    final ChannelPrefix channelPrefix = new ChannelPrefix(prefix, channel);
                    // 添加到頻道列表
                    channels.add(channelPrefix);
                }
            }
        }

        // 最後處理 `DefaultChannel:` 內容
        final String defaultChannel = file.getString("DefaultChannel");
        if (defaultChannel != null) {
            // 創建頻道前綴
            final ChannelPrefix channelPrefix = new DefaultChannelPrefix(defaultChannel);
            // 添加到頻道列表
            channels.add(channelPrefix);
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
