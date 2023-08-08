package net.moubiecat.chatcontrol.database;

import org.apache.ibatis.annotations.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface Database {
    String TABLE_NAME = "ChatControl_First";

    @Update("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (UUID VARCHAR(36) NOT NULL PRIMARY KEY, FIRST BOOLEAN NOT NULL)")
    void createTable();

    @Select("SELECT * FROM " + TABLE_NAME + " WHERE UUID = #{uuid}")
    @Results({
            @Result(column = "UUID", property = "uuid", javaType = UUID.class),
            @Result(column = "FIRST", property = "first", javaType = boolean.class)
    })
    @Nullable Data selectData(@Param("uuid") UUID player);

    @Insert("INSERT INTO " + TABLE_NAME + " (UUID, FIRST) VALUES (#{uuid}, #{first})")
    void insertData(@NotNull Data data);

    @Update("UPDATE " + TABLE_NAME + " SET FIRST = #{first} WHERE UUID = #{uuid}")
    void updateData(@NotNull Data data);

    @Delete("DELETE FROM " + TABLE_NAME + " WHERE UUID = #{uuid}")
    void deleteData(@Param("uuid") @NotNull UUID player);
}
