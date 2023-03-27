package com.kopylov.orm;

import com.kopylov.orm.annotation.Column;
import com.kopylov.orm.annotation.Table;

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
    public String findById(Class<?> personClass, Object id) {
        Table tableAnnotation = getAnnotationTable(personClass);
        String tableName = getTableName(personClass, tableAnnotation);
        StringBuilder query = new StringBuilder("SELECT ");
        StringJoiner columnNames = getAllFieldsWithoutId(personClass);
        query.append(columnNames);
        query.append(" FROM ");
        query.append(tableName);
        query.append(" WHERE id=");
        query.append(wrapId(id));
        query.append(";");
        return query.toString();
    }

    @Override
    public String deleteById(Class<?> personClass, Object id) {
        Table tableAnnotation = getAnnotationTable(personClass);
        String tableName = getTableName(personClass, tableAnnotation);
        StringBuilder query = new StringBuilder("DELETE ");
        query.append("FROM ");
        query.append(tableName);
        query.append(" WHERE id=");
        query.append(wrapId(id));
        query.append(";");
        return query.toString();
    }

    @Override
    public String insert(Object object) throws IllegalAccessException {
        Table tableAnnotation = getAnnotationTable(object.getClass());
        String tableName = getTableName(object.getClass(), tableAnnotation);
        StringBuilder query = new StringBuilder("INSERT INTO ");
        StringJoiner columnNames = getAllFields(object.getClass());
        String values = getValuesFromObject(object);
        query.append(tableName);
        query.append(" (");
        query.append(columnNames);
        query.append(")");
        query.append(" VALUES ");
        query.append(values);
        query.append(";");
        return query.toString();
    }

    @Override
    public String update(Object object) throws IllegalAccessException {
        Table tableAnnotation = getAnnotationTable(object.getClass());
        String tableName = getTableName(object.getClass(), tableAnnotation);
        StringBuilder query = new StringBuilder("UPDATE ");
        query.append(tableName);
        query.append(" SET ");
        query.append(getColumnNameAndFieldFromObject(object));
        query.append(" WHERE id=");
        query.append(getIdFromObject(object));
        query.append(";");
        return query.toString();
    }

    private StringJoiner getColumnNameAndFieldFromObject(Object object) throws IllegalAccessException {
        Class<?> personClass = object.getClass();
        StringJoiner stringJoiner = new StringJoiner(", ");
        for (Field declaredField : personClass.getDeclaredFields()) {
            Column columnAnnotation = declaredField.getAnnotation(Column.class);
            if (columnAnnotation != null) {
                if (!columnAnnotation.name().isEmpty()) {
                    declaredField.setAccessible(true);
                    if (columnAnnotation.name().contains("person_name")) {
                        stringJoiner.add(columnAnnotation.name() + "=" + "'" + declaredField.get(object).toString() + "'");
                    } else {
                        stringJoiner.add(columnAnnotation.name() + "=" + declaredField.get(object).toString());
                    }
                }
            }
        }
        return stringJoiner;
    }

    private Table getAnnotationTable(Class<?> personClass) {
        if (!personClass.isAnnotationPresent(Table.class)) {
            throw new IllegalArgumentException("Class should be annotated with @Table");
        }
        return personClass.getAnnotation(Table.class);
    }

    private String getTableName(Class<?> personClass, Table tableAnnotation) {
        return tableAnnotation.name().isEmpty() ? personClass.getSimpleName() : tableAnnotation.name();
    }

    private StringJoiner getAllFields(Class<?> personClass) {
        StringJoiner columnNames = new StringJoiner(", ");
        for (Field declaredField : personClass.getDeclaredFields()) {
            Column columnAnnotation = declaredField.getAnnotation(Column.class);
            if (columnAnnotation != null) {
                columnNames.add(columnAnnotation.name().isEmpty() ? declaredField.getName() : columnAnnotation.name());
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
                    columnNames.add(columnAnnotation.name());
                }
            }
        }
        return columnNames;
    }

    private String getIdFromObject(Object object) throws IllegalAccessException {
        Class<?> personClass = object.getClass();
        for (Field declaredField : personClass.getDeclaredFields()) {
            Column columnAnnotation = declaredField.getAnnotation(Column.class);
            if (columnAnnotation != null) {
                if (columnAnnotation.name().isEmpty()) {
                    declaredField.setAccessible(true);
                    return declaredField.get(object).toString();
                }
            }
        }
        return null;
    }

    private String getValuesFromObject(Object object) throws IllegalAccessException {
        Class<?> personClass = object.getClass();
        StringJoiner columnNames = new StringJoiner(", ", "(", ")");
        for (Field declaredField : personClass.getDeclaredFields()) {
            Column columnAnnotation = declaredField.getAnnotation(Column.class);
            if (columnAnnotation != null) {
                declaredField.setAccessible(true);
                if (columnAnnotation.name().contains("person_name")) {
                    columnNames.add("'" + declaredField.get(object).toString() + "'");
                } else {
                    columnNames.add(declaredField.get(object).toString());
                }
            }
        }
        return columnNames.toString();
    }

    private String wrapId(Object id) {
        if (id instanceof String) {
            return "'" + id + "'";
        }else{
            return id.toString();
        }
    }

}
