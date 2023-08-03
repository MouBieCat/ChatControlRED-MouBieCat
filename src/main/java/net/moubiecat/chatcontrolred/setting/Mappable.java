package net.moubiecat.chatcontrolred.setting;

import org.jetbrains.annotations.NotNull;

public interface Mappable<T extends Yaml> {
    /**
     * 載入
     *
     * @param file 檔案
     */
    void onLoad(@NotNull T file);

    /**
     * 重新載入
     *
     * @param file 檔案
     */
    void onReload(@NotNull T file);

    /**
     * 儲存
     *
     * @param file 檔案
     */
    void onSave(@NotNull T file);
}
