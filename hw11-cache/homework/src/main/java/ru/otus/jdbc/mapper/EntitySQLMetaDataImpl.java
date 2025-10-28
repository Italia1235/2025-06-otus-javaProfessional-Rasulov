package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {
    EntityClassMetaData<?> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return "SELECT * FROM " + entityClassMetaData.getName();
    }

    @Override
    public String getSelectByIdSql() {

        return "SELECT * FROM " + entityClassMetaData.getName() +
                " WHERE " + entityClassMetaData.getIdField().getName() + "= ?";
    }

    @Override
    public String getInsertSql() {
        var fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        String columns = fieldsWithoutId.stream()
                .map(Field::getName)
                .collect(Collectors.joining(", "));
        String placeholders = fieldsWithoutId.stream()
                .map(f -> "?")
                .collect(Collectors.joining(", "));
        return "INSERT INTO " + entityClassMetaData.getName() +
                " (" + columns + ") VALUES (" + placeholders + ")";
    }

    @Override
    public String getUpdateSql() {
        String setStatements = entityClassMetaData.getFieldsWithoutId().stream()
                .map(field -> field.getName() + " = ?")
                .collect(Collectors.joining(", "));

        return "UPDATE " + entityClassMetaData.getName() + " SET " + setStatements + " WHERE " + entityClassMetaData.getIdField().getName() + " = ?";

    }
}
