package net.moubiecat.chatcontrol.channel;

import net.moubiecat.chatcontrol.ChannelYaml;
import net.moubiecat.chatcontrol.MouBieCat;
import net.moubiecat.chatcontrol.utils.Configurable;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class ChannelManager {
    private final List<IChannel> channels = new LinkedList<>();
    private final ChannelLoader loader = new ChannelLoader(this);

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
    public @Nullable IChannel getChannel(@NotNull String name) {
        for (final IChannel channel : this.channels)
            if (channel.getChannelName().equalsIgnoreCase(name))
                return channel;
        return null;
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
     * 列印頻道
     */
    public void printChannels() {
        this.channels.forEach(channel -> MouBieCat.getInstance().getLogger().info(channel.getChannelName() + ": " + channel.getPrefix()));
    }

    /**
     * 取得頻道載入器
     *
     * @return 頻道載入器
     */
    public @NotNull ChannelLoader getLoader() {
        return this.loader;
    }

    public final static class ChannelLoader implements Configurable<ChannelYaml> {
        private final ChannelManager manager;

        /**
         * 建構子
         *
         * @param manager 頻道管理器
         */
        public ChannelLoader(@NotNull ChannelManager manager) {
            this.manager = manager;
        }

        /**
         * Load config
         *
         * @param config config
         */
        @Override
        public void onLoad(@NotNull ChannelYaml config) {
            this.getChannel(config).forEach(manager::addChannel);
            manager.printChannels();
        }

        /**
         * Save config
         *
         * @param config config
         */
        @Override
        public void onSave(@NotNull ChannelYaml config) {
            // 沒有事情要做
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
            manager.printChannels();
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
