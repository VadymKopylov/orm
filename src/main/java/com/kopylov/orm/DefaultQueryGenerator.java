package com.kopylov.orm;

import com.kopylov.orm.annotation.Column;
import com.kopylov.orm.annotation.Table;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.StringJoiner;

public class DefaultQueryGenerator implements QueryGenerator {
    @Override
    public String findAll(Class<?> personClass) {
        Table tableAnnotation = getAnnotationTable(personClass);
        String tableName = getTableName(personClass, tableAnnotation);
        StringBuilder query = new StringBuilder("SELECT ");
        StringJoiner columnNames = getAllFields(personClass);
        query.append(columnNames);
        query.append(" FROM ");
        query.append(tableName);
        query.append(";");
        return query.toString();
    }

    @Override
    public String findById(Class<?> personClass, Serializable id) {
        Table tableAnnotation = getAnnotationTable(personClass);
        String tableName = getTableName(personClass, tableAnnotation);
        StringBuilder query = new StringBuilder("SELECT ");
        StringJoiner columnNames = getAllFieldsWithoutId(personClass);
        query.append(columnNames);
        query.append(" FROM ");
        query.append(tableName);
        query.append(" WITH id: ");
        query.append(id);
        return query.toString();
    }

    @Override
    public String deleteById(Class<?> personClass, Serializable id) {
        Table tableAnnotation = getAnnotationTable(personClass);
        String tableName = getTableName(personClass, tableAnnotation);
        StringBuilder query = new StringBuilder("DELETE ");
        query.append("FROM ");
        query.append(tableName);
        query.append(" WITH id: ");
        query.append(id);

        return query.toString();
    }

    @Override
    public String insert(Object object) throws IllegalAccessException {
        Table tableAnnotation = getAnnotationTable(object.getClass());
        String tableName = getTableName(object.getClass(), tableAnnotation);
        StringBuilder query = new StringBuilder("INSERT ");
        StringJoiner columnNames = getAllFields(object.getClass());
        String values = getValuesFromObject(object);
        query.append(columnNames);
        query.append(" TO ");
        query.append(tableName);
        query.append(" WITH values ");
        query.append(values);
        return query.toString();
    }

    @Override
    public String update(Object object) throws IllegalAccessException {
        Table tableAnnotation = getAnnotationTable(object.getClass());
        String tableName = getTableName(object.getClass(), tableAnnotation);
        StringBuilder query = new StringBuilder("UPDATE ");
        StringJoiner columnNames = getAllFieldsWithoutId(object.getClass());
        int id = getIdFromObject(object);
        query.append(columnNames);
        query.append(" TO ");
        query.append(tableName);
        query.append(" WITH id ");
        query.append(id);
        return query.toString();
    }

    private Table getAnnotationTable(Class<?> personClass) {
        Table tableAnnotation = personClass.getAnnotation(Table.class);
        if (tableAnnotation == null) {
            throw new IllegalArgumentException();
        }
        return tableAnnotation;
    }

    private String getTableName(Class<?> personClass, Table tableAnnotation) {
        return tableAnnotation.name().isEmpty() ? personClass.getSimpleName() : tableAnnotation.name();
    }

    private StringJoiner getAllFields(Class<?> personClass) {
        StringJoiner columnNames = new StringJoiner(", ");
        for (Field declaredField : personClass.getDeclaredFields()) {
            Column columnAnnotation = declaredField.getAnnotation(Column.class);
            if (columnAnnotation != null) {
                String columnName = columnAnnotation.name().isEmpty() ? declaredField.getName() : columnAnnotation.name();
                columnNames.add(columnName);
            }
        }
        return columnNames;
    }

    private StringJoiner getAllFieldsWithoutId(Class<?> personClass) {
        StringJoiner columnNames = new StringJoiner(", ");
        for (Field declaredField : personClass.getDeclaredFields()) {
            Column columnAnnotation = declaredField.getAnnotation(Column.class);
            if (columnAnnotation != null) {
                if (!columnAnnotation.name().isEmpty()) {
                    String columnName = columnAnnotation.name();
                    columnNames.add(columnName);
                }
            }
        }
        return columnNames;
    }

    private int getIdFromObject(Object object) throws IllegalAccessException {
        int id = 0;
        Class<?> personClass = object.getClass();
        for (Field declaredField : personClass.getDeclaredFields()) {
            Column columnAnnotation = declaredField.getAnnotation(Column.class);
            if (columnAnnotation != null) {
                if (columnAnnotation.name().isEmpty()) {
                    declaredField.setAccessible(true);
                    id = declaredField.getInt(object);
                }
            }
        }
        return id;
    }

    private String getValuesFromObject(Object object) throws IllegalAccessException {
        Class<?> personClass = object.getClass();
        StringJoiner columnNames = new StringJoiner(", ");
        for (Field declaredField : personClass.getDeclaredFields()) {
            Column columnAnnotation = declaredField.getAnnotation(Column.class);
            if (columnAnnotation != null) {
                declaredField.setAccessible(true);
                Object value = declaredField.get(object);
                columnNames.add(value.toString());
            }
        }
        return columnNames.toString();
    }
}
