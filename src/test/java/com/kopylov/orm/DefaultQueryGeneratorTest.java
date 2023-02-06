package com.kopylov.orm;

import com.kopylov.orm.entity.Person;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DefaultQueryGeneratorTest {

    @Test
    public void testGenerateSelectAll() {
        String expectedQuery = "SELECT id, person_name, person_salary FROM Person;";
        DefaultQueryGenerator queryGenerator = new DefaultQueryGenerator();
        String actualQuery = queryGenerator.findAll(Person.class);
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGenerateFindById() {
        String expectedQuery = "SELECT person_name, person_salary FROM Person WHERE id=1;";
        DefaultQueryGenerator queryGenerator = new DefaultQueryGenerator();
        String actualQuery = queryGenerator.findById(Person.class, 1);
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testDeleteById() {
        String expectedQuery = "DELETE FROM Person WHERE id=1;";
        DefaultQueryGenerator queryGenerator = new DefaultQueryGenerator();
        String actualQuery = queryGenerator.deleteById(Person.class,1);
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testInsert() throws IllegalAccessException {
        String expectedQuery = "INSERT INTO Person (id, person_name, person_salary) VALUES ('2', 'Tom', '1000.0');";
        Person person = new Person(2, "Tom", 1000.0);
        DefaultQueryGenerator queryGenerator = new DefaultQueryGenerator();
        String actualQuery = queryGenerator.insert(person);
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testUpdate() throws IllegalAccessException {
        String expectedQuery = "UPDATE Person SET person_name=Tom, person_salary=2000.0 WHERE id=1;";
        Person person = new Person(1, "Tom", 2000.0);
        DefaultQueryGenerator queryGenerator = new DefaultQueryGenerator();
        String actualQuery = queryGenerator.update(person);
        assertEquals(expectedQuery, actualQuery);
    }
}
