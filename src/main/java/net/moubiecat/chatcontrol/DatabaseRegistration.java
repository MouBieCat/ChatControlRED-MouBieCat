package net.moubiecat.chatcontrol;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.jetbrains.annotations.NotNull;
import org.mineacademy.chatcontrol.lib.settings.YamlConfig;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DatabaseRegistration {
    public static SqlSessionFactory SQL_SESSION_FACTORY = null;

    private final List<Class<?>> mapperList = new ArrayList<>();
    private final Map<Class<?>, Class<?>> typeHandler = new HashMap<>();

    /**
     * 註冊 Mapper
     *
     * @param mapper Mapper
     */
    public void registerMapper(@NotNull Class<?> mapper) {
        this.mapperList.add(mapper);
    }

    /**
     * 註冊 TypeHandler
     *
     * @param typeHandler TypeHandler
     */
    public void registerTypeHandler(@NotNull Class<?> clazz, @NotNull Class<?> typeHandler) {
        this.typeHandler.put(clazz, typeHandler);
    }

    /**
     * 綁定 SQL 會話工廠
     */

    public void buildSqlSessionFactory() {
        // 獲取資料庫設定
        final YamlConfig config = YamlConfig.fromInternalPath("mysql.yml");
        final String host = config.getString("Host");
        final String database = config.getString("Database");
        final String user = config.getString("User");
        final String password = config.getString("Password");
        final String line = config.getString("Line");
        // 建立資料源
        final DataSource dataSource = new PooledDataSource(
                "com.mysql.jdbc.Driver",
                line.replace("{host}", host).replace("{database}", database),
                user, password);
        // 建立環境
        final Environment environment = new Environment("development", new JdbcTransactionFactory(), dataSource);
        // 建立配置
        final Configuration configuration = new Configuration(environment);
        // 註冊 TypeHandler
        this.typeHandler.forEach(configuration.getTypeHandlerRegistry()::register);
        // 註冊 Mapper
        this.mapperList.forEach(configuration::addMapper);
        // 建立 SqlSessionFactory
        DatabaseRegistration.SQL_SESSION_FACTORY = new SqlSessionFactoryBuilder().build(configuration);
    }
}
