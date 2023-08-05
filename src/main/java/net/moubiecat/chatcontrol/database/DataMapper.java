package net.moubiecat.chatcontrol.database;

import org.apache.ibatis.annotations.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface DataMapper {
    String TABLE_NAME = "ChatControl_First";

    @Select("SELECT * FROM " + TABLE_NAME + " WHERE UUID = #{player}")
    @Results({
            @Result(column = "UUID", property = "player", javaType = UUID.class),
            @Result(column = "FIRST", property = "first", javaType = boolean.class)
    })
    @Nullable Data selectData(@Param("player") UUID player);

    @Insert("INSERT INTO " + TABLE_NAME + " (UUID, FIRST) VALUES (#{player}, #{first})")
    void insertData(@NotNull Data data);

    @Update("UPDATE " + TABLE_NAME + " SET FIRST = #{first} WHERE UUID = #{player}")
    void updateData(@NotNull Data data);

    @Delete("DELETE FROM " + TABLE_NAME + " WHERE UUID = #{player}")
    void deleteData(@Param("player") @NotNull UUID player);
}
