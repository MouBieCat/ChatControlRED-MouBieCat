package net.moubiecat.chatcontrol.database;

import net.moubiecat.chatcontrol.MouBieCat;
import org.apache.ibatis.session.SqlSession;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public final class PlayerDatabase implements Database {
    @Override
    public void createTable() {
        try (final SqlSession sqlSession = MouBieCat.getSqlSessionFactory().openSession()) {
            final Database dataMapper = sqlSession.getMapper(Database.class);
            dataMapper.createTable();
            sqlSession.commit();
        }
    }

    @Override
    public @Nullable Data selectData(@NotNull UUID player) {
        try (final SqlSession sqlSession = MouBieCat.getSqlSessionFactory().openSession()) {
            final Database dataMapper = sqlSession.getMapper(Database.class);
            return dataMapper.selectData(player);
        }
    }

    @Override
    public void insertData(@NotNull Data data) {
        try (final SqlSession sqlSession = MouBieCat.getSqlSessionFactory().openSession()) {
            final Database dataMapper = sqlSession.getMapper(Database.class);
            dataMapper.insertData(data);
            sqlSession.commit();
        }
    }

    @Override
    public void updateData(@NotNull Data data) {
        try (final SqlSession sqlSession = MouBieCat.getSqlSessionFactory().openSession()) {
            final Database dataMapper = sqlSession.getMapper(Database.class);
            dataMapper.updateData(data);
            sqlSession.commit();
        }
    }

    @Override
    public void deleteData(@NotNull UUID player) {
        try (final SqlSession sqlSession = MouBieCat.getSqlSessionFactory().openSession()) {
            final Database dataMapper = sqlSession.getMapper(Database.class);
            dataMapper.deleteData(player);
            sqlSession.commit();
        }
    }
}
