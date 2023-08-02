package net.moubiecat.chatcontrolred.channel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ChannelItem {
    /**
     * 獲取頻道名稱
     *
     * @return 名稱
     */
    @NotNull String getChannel();

    /**
     * 獲取頻道顯示名稱
     *
     * @return 名稱
     */
    @NotNull String getDisplay();

    /**
     * 獲取頻道描述
     *
     * @return 描述
     */
    @NotNull List<String> getLore();
}
