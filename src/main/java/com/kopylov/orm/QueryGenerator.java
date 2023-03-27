package com.kopylov.orm;

public interface QueryGenerator {

    String findAll(Class<?> type);

    String findById(Class<?> type, Object id);

    String deleteById(Class<?> type, Object id);

    String insert(Object object) throws IllegalAccessException;

    String update(Object object) throws IllegalAccessException;

}
