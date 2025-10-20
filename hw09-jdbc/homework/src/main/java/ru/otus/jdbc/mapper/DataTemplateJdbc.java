package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
@SuppressWarnings("java:S1068")
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;

    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.<T>executeSelect(
                connection,
                entitySQLMetaData.getSelectByIdSql(),
                List.of(id),
                rs -> {
                    try {
                        if (rs.next()) {
                            Constructor<T> constructor = entityClassMetaData.getConstructor();
                            var object = constructor.newInstance();

                            for (Field field : entityClassMetaData.getAllFields()) {
                                field.setAccessible(true);
                                Object value = rs.getObject(field.getName());
                                field.set(object, value);
                            }

                            return object;
                        }
                        return null;
                    } catch (SQLException | IllegalAccessException | InstantiationException |
                             InvocationTargetException | NoSuchMethodException e) {
                        throw new DataTemplateException(e);
                    }
                }
        );
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.<List<T>>executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
            var objectList = new ArrayList<T>();
            try {
                while (rs.next()) {
                    Constructor<T> constructor = entityClassMetaData.getConstructor();
                    var object = constructor.newInstance();

                    for (Field field : entityClassMetaData.getAllFields()) {
                        field.setAccessible(true);
                        Object value = rs.getObject(field.getName());
                        field.set(object, value);
                    }
                    objectList.add(object);
                }
                return objectList;
            } catch (SQLException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                     InvocationTargetException e) {
                throw new DataTemplateException(e);
            }
        }).orElse(Collections.emptyList());
    }

    @Override
    public long insert(Connection connection, T entity) {
        try {
            List<Object> params = entityClassMetaData.getFieldsWithoutId().stream()
                    .peek(it -> it.setAccessible(true))
                    .map(field -> {
                        try {
                            return field.get(entity);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }).toList();
            return dbExecutor.executeStatement(
                    connection, entitySQLMetaData.getInsertSql(), params);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T entity) {
        try {
            List<Object> params = new ArrayList<>(entityClassMetaData.getFieldsWithoutId().stream()
                    .peek(it -> it.setAccessible(true))
                    .map(field -> {
                        try {
                            return field.get(entity);
                        } catch (IllegalAccessException e) {
                            throw new DataTemplateException(e);
                        }
                    }).toList());

            Field idField = entityClassMetaData.getIdField();
            idField.setAccessible(true);

            params.add(idField.get(entity));
            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), params);
        } catch (IllegalAccessException e) {
            throw new DataTemplateException(e);

        }

    }
}
