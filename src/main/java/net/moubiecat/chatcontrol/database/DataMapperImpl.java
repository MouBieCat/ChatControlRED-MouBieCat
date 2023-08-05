package net.moubiecat.chatcontrol.database;

import net.moubiecat.chatcontrol.MouBieCat;
import org.apache.ibatis.session.SqlSession;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public final class DataMapperImpl implements DataMapper {
    @Override
    public @Nullable Data selectData(@NotNull UUID player) {
        try (SqlSession sqlSession = MouBieCat.getSqlSessionFactory().openSession()) {
            final DataMapper dataMapper = sqlSession.getMapper(DataMapper.class);
            return dataMapper.selectData(player);
        }
    }

    @Override
    public void insertData(@NotNull Data data) {
        try (SqlSession sqlSession = MouBieCat.getSqlSessionFactory().openSession()) {
            final DataMapper dataMapper = sqlSession.getMapper(DataMapper.class);
            dataMapper.insertData(data);
            sqlSession.commit();
        }
    }

    @Override
    public void updateData(@NotNull Data data) {
        try (SqlSession sqlSession = MouBieCat.getSqlSessionFactory().openSession()) {
            final DataMapper dataMapper = sqlSession.getMapper(DataMapper.class);
            dataMapper.updateData(data);
            sqlSession.commit();
        }
    }

    @Override
    public void deleteData(@NotNull UUID player) {
        try (SqlSession sqlSession = MouBieCat.getSqlSessionFactory().openSession()) {
            final DataMapper dataMapper = sqlSession.getMapper(DataMapper.class);
            dataMapper.deleteData(player);
            sqlSession.commit();
        }
    }
}
