package net.moubiecat.chatcontrol.database;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.chatcontrol.lib.Valid;
import org.mineacademy.chatcontrol.lib.database.SimpleDatabase;
import org.mineacademy.chatcontrol.lib.settings.YamlConfig;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * 套用了 ChatControlRed 的代碼
 * 該插件是閉源項目我只是間接使用了他的代碼
 *
 * @author kangarko <a href="https://github.com/kangarko">前往</a>
 */
public final class FirstChatDatabase extends SimpleDatabase implements DAO<PlayerFirstChat> {
    private static final String TABLE_NAME = "ChatControl_First";

    /**
     * 建構子
     */
    public FirstChatDatabase() {
        this.addVariable("table_first", TABLE_NAME);
    }

    /**
     * 連接資料庫
     */
    public void connect() {
        final YamlConfig config = YamlConfig.fromInternalPath("mysql.yml");
        final String host = config.getString("Host");
        final String database = config.getString("Database");
        final String user = config.getString("User");
        final String password = config.getString("Password");
        final String line = config.getString("Line");
        this.connect(line.replace("{host}", host).replace("{database}", database), user, password, "ChatControl");
    }

    @Override
    protected void onConnected() {
        this.createTable(TableCreator.of(TABLE_NAME).addNotNull("UUID", "varchar(64)").add("FIRST", "BOOLEAN").setPrimaryColumn("UUID"));
    }

    /**
     * 插入一筆資料
     *
     * @param data 資料
     */
    @Override
    public void insert(@NotNull PlayerFirstChat data) {
        Valid.checkSync("Cannot insert data synchronously!");
        // 插入資料
        try (final PreparedStatement statement = this.getConnection().prepareStatement("INSERT INTO " + TABLE_NAME + " (`UUID`, `FIRST`) VALUES (?, ?)")) {
            // 設定資料 `player`
            statement.setString(1, data.getPlayer().toString());
            // 設定資料 `first`
            statement.setBoolean(2, data.isFirst());
            statement.executeUpdate();
        } catch (final SQLIntegrityConstraintViolationException ignored) {
            // 主鍵重複，不做任何事。
        } catch (final SQLException e) {
            // 其他錯誤，輸出錯誤訊息。
            e.printStackTrace();
        }
    }

    /**
     * 更新一筆資料
     *
     * @param data 資料
     */
    @Override
    public void update(@NotNull PlayerFirstChat data) {
        Valid.checkSync("Cannot insert data synchronously!");
        // 更新資料
        try (final PreparedStatement statement = this.getConnection().prepareStatement("UPDATE " + TABLE_NAME + " SET `FIRST` = ? WHERE `UUID` = ?")) {
            // 設定資料 `first`
            statement.setBoolean(1, data.isFirst());
            // 設定資料 `player`
            statement.setString(2, data.getPlayer().toString());
            statement.executeUpdate();
        } catch (final Exception e) {
            // 輸出錯誤訊息。
            e.printStackTrace();
        }
    }

    /**
     * 刪除一筆資料
     *
     * @param data 資料
     */
    @Override
    public void remove(@NotNull PlayerFirstChat data) {
        Valid.checkSync("Cannot insert data synchronously!");
        // 刪除資料
        try (final PreparedStatement statement = this.getConnection().prepareStatement("DELETE FROM " + TABLE_NAME + " WHERE `UUID` = ?")) {
            // 設定資料 `player`
            statement.setString(1, data.getPlayer().toString());
            statement.executeUpdate();
        } catch (final SQLException e) {
            // 輸出錯誤訊息。
            e.printStackTrace();
        }
    }

    /**
     * 查詢多筆資料
     *
     * @param key 查詢主鍵
     * @return 資料清單
     */
    @Override
    public @Nullable PlayerFirstChat select(@NotNull String key) {
        try (final PreparedStatement statement = this.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE `UUID` = ?")) {
            statement.setString(1, key);

            final ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
                return new PlayerFirstChat(UUID.fromString(key), resultSet.getBoolean("FIRST"));
        } catch (final SQLException e) {
            // 輸出錯誤訊息。
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查詢所有資料
     *
     * @return 資料清單
     */
    @Override
    public @NotNull List<PlayerFirstChat> selectAll() {
        final List<PlayerFirstChat> data = new LinkedList<>();
        try (final PreparedStatement statement = this.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME)) {
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
                data.add(new PlayerFirstChat(
                        UUID.fromString(resultSet.getString("UUID")),
                        resultSet.getBoolean("FIRST")));
        } catch (final SQLException e) {
            // 輸出錯誤訊息。
            e.printStackTrace();
        }

        return data;
    }
}
