package net.moubiecat.chatcontrolred.channel;

import net.moubiecat.chatcontrolred.MouBieCat;
import net.moubiecat.chatcontrolred.setting.ConfigYaml;
import net.moubiecat.chatcontrolred.setting.Mappable;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public final class ChannelManager implements Mappable<ConfigYaml> {
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
            if (channel.sendMessageToChannel(player, message))
                return;
        }
    }

    @NotNull
    public Collection<ChannelPrefix> getChannels() {
        return this.channels;
    }

    @Override
    public void onLoad(@NotNull ConfigYaml file) {
        // 解析頻道列表
        this.parseChannelPrefix(file);
        this.printChannels();
    }

    @Override
    public void onReload(@NotNull ConfigYaml file) {
        // 清空頻道列表
        this.channels.clear();
        // 重新解析頻道列表
        this.parseChannelPrefix(file);
        this.printChannels();
    }

    @Override
    public void onSave(@NotNull ConfigYaml file) {
        // 沒有任何需要保存的內容
    }

    /**
     * 獲取頻道列表
     *
     * @param file 配置文件
     */
    private void parseChannelPrefix(@NotNull ConfigYaml file) {
        // 獲取所有頻道前綴
        final Set<String> channels = file.getChannels();

        // 遍歷所有頻道前綴
        for (final String channel : channels) {
            // 解析頻道名稱、顯示名稱、描述
            final String channelName = file.getChannel(channel);
            final String channelDisplay = file.getChannelDisplay(channel);
            final List<String> channelLore = file.getChannelLore(channel);

            // 添加頻道前綴
            this.channels.add(new ChannelPrefix(channel, channelName, channelDisplay, channelLore));
        }

        // 獲取預設頻道前綴
        final String defaultChannel = file.getDefaultChannel();
        final String defaultChannelDisplay = file.getDefaultChannelDisplay();
        final List<String> defaultChannelLore = file.getDefaultChannelLore();

        // 添加預設頻道前綴
        this.channels.add(new DefaultChannelPrefix(defaultChannel, defaultChannelDisplay, defaultChannelLore));
    }

    public void printChannels() {
        this.channels.forEach(
                channelPrefix -> MouBieCat.getInstance().getLogger().info(channelPrefix.getPrefix() + " -> " + channelPrefix.getChannel()));
    }
}
