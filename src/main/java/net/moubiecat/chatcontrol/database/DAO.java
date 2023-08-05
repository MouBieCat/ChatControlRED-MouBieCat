package net.moubiecat.chatcontrol.database;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface DAO<T> {
    /**
     * 插入一筆資料
     *
     * @param t 資料
     */
    void insert(@NotNull T t);

    /**
     * 更新一筆資料
     *
     * @param t 資料
     */
    void update(@NotNull T t);

    /**
     * 刪除一筆資料
     *
     * @param t 資料
     */
    void remove(@NotNull T t);

    /**
     * 查詢多筆資料
     *
     * @param key 查詢主鍵
     * @return 資料清單
     */
    @Nullable T select(@NotNull String key);

    /**
     * 查詢所有資料
     *
     * @return 資料清單
     */
    @NotNull List<T> selectAll();
}
