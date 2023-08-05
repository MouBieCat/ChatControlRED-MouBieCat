package net.moubiecat.chatcontrol.injector;

import com.google.inject.Inject;
import net.moubiecat.chatcontrol.MouBieCat;
import net.moubiecat.chatcontrol.channel.IChannel;
import net.moubiecat.chatcontrol.channel.MBChannel;
import net.moubiecat.chatcontrol.channel.MBDefaultChannel;
import net.moubiecat.chatcontrol.utils.Configurable;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public final class ChannelManager {
    private final List<IChannel> channels = new LinkedList<>();

    /**
     * 添加頻道
     *
     * @param channel 頻道
     */
    private void addChannel(@NotNull IChannel channel) {
        this.channels.add(channel);
    }

    /**
     * 取得所有頻道
     *
     * @return 頻道列表
     */
    public @NotNull List<IChannel> getChannels() {
        return new LinkedList<>(this.channels);
    }

    /**
     * 取得頻道
     *
     * @param name 頻道名稱
     * @return 頻道
     */
    public @NotNull Optional<IChannel> getChannel(@NotNull String name) {
        for (final IChannel channel : this.channels)
            if (channel.getChannelName().equalsIgnoreCase(name))
                return Optional.of(channel);
        return Optional.empty();
    }

    /**
     * 發送訊息
     *
     * @param player  玩家
     * @param message 訊息
     */
    public boolean sendMessage(@NotNull Player player, @Nullable String message) {
        // 如果訊息為空，則不處理
        if (message == null)
            return false;

        // 發送到指定頻道
        for (final IChannel channel : this.channels) {
            // 如果發送成功，則不再繼續
            if (channel.sendMessage(player, message))
                // 不是預設頻道，則返回 true
                return true;
        }

        // 執行到這裡，一定是 MBDefaultChannel，所以直接返回 false
        return false;
    }

    /**
     * 取得頻道載入器
     *
     * @return 頻道載入器
     */
    public @NotNull ChannelConfiguration getParser() {
        return MouBieCat.getInstance(ChannelConfiguration.class);
    }

    public final static class ChannelConfiguration implements Configurable<ChannelYaml> {
        @Inject
        private ChannelManager manager;

        /**
         * Load config
         *
         * @param config config
         */
        @Override
        public void onLoad(@NotNull ChannelYaml config) {
            this.getChannel(config).forEach(manager::addChannel);
        }

        /**
         * Reload config
         *
         * @param config config
         */
        @Override
        public void onReload(@NotNull ChannelYaml config) {
            manager.channels.clear();
            this.getChannel(config).forEach(manager::addChannel);
        }

        /**
         * 解析所有頻道
         *
         * @param config config
         * @return 頻道列表
         */
        private @NotNull List<IChannel> getChannel(@NotNull ChannelYaml config) {
            final List<IChannel> channels = new ArrayList<>();
            for (final String name : config.getChannelNames()) {
                final String channelPrefix = config.getChannelPrefix(name);
                final String channelName = config.getChannelName(name);
                final Material channelIcon = config.getChannelIcon(name);
                final String channelDisplay = config.getChannelDisplay(name);
                final List<String> channelLore = config.getChannelLore(name);

                // 如果是預設頻道，則使用 MBDefaultChannel
                if (name.equalsIgnoreCase("default")) {
                    channels.add(new MBDefaultChannel(channelName, channelIcon, channelDisplay, channelLore));
                    continue;
                }
                // 否則使用 MBChannel
                channels.add(new MBChannel(channelPrefix, channelName, channelIcon, channelDisplay, channelLore));
            }

            return channels;
        }
    }
}
