package ru.otus.jdbc.mapper;

import ru.otus.annotation.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    Class<T> entityClass;
    private final String name;
    private final Constructor constructor;
    private final List<Field> fields;

    private final Field idField;

    private final List<Field> fieldsWithoutId;

    public EntityClassMetaDataImpl(Class<T> entityClass) {
        this.name = entityClass.getSimpleName();
        this.fields = List.of(entityClass.getDeclaredFields());
        this.idField = findIdField();
        this.fieldsWithoutId = fields.stream().filter(it -> !it.equals(this.idField)).toList();
        try {
            this.constructor = entityClass.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Constructor getConstructor() throws NoSuchMethodException {
        return this.constructor;
    }

    @Override
    public Field getIdField() {
        return this.idField;
    }

    @Override
    public List<Field> getAllFields() {
        return this.fields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return this.fieldsWithoutId;
    }

    private Field findIdField() {
        return this.fields.stream()
                .filter(f -> f.isAnnotationPresent(Id.class))
                .reduce((first, second) -> {
                    throw new IllegalStateException(
                            "Entity class " + entityClass.getSimpleName() + " has multiple @Id fields");
                })
                .orElseThrow(() -> new IllegalStateException(
                        "Entity class " + entityClass.getSimpleName() + " does not have an @Id field"));
    }
}
