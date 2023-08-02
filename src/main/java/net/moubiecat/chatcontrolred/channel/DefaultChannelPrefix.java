package net.moubiecat.chatcontrolred.channel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DefaultChannelPrefix extends ChannelPrefix {
    public DefaultChannelPrefix(@NotNull String channel, @NotNull String display, @NotNull List<String> lore) {
        super("", channel, display, lore);
    }

    /**
     * 判斷頻道前綴是否相同
     * 該方法已經被重寫，所以永遠返回 true
     *
     * @param message 訊息
     * @return 是否相同
     */
    @Override
    protected boolean checkChannelPrefix(@Nullable String message) {
        // 該方法已經被重寫，所以永遠返回 true
        return true;
    }
}
