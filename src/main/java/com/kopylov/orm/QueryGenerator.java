package com.kopylov.orm;

import java.io.Serializable;

public interface QueryGenerator {

    String findAll(Class<?> type);

    String findById(Class<?> type, Serializable id);

    String deleteById(Class<?> type, Serializable id);

    String insert(Object object) throws IllegalAccessException;

    String update(Object object) throws IllegalAccessException, NoSuchFieldException;

}
