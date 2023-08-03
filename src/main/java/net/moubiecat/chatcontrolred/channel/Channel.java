package net.moubiecat.chatcontrolred.channel;

import org.jetbrains.annotations.NotNull;

public interface Channel {
    /**
     * 獲取前綴
     *
     * @return 前綴
     */
    @NotNull String getPrefix();

    /**
     * 獲取頻道名稱
     *
     * @return 名稱
     */
    @NotNull String getChannel();
}
