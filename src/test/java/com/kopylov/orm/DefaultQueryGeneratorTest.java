package com.kopylov.orm;

import com.kopylov.orm.entity.Person;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DefaultQueryGeneratorTest {

    Person person = new Person(1, "John", 1500);

    @Test
    public void testGenerateSelectAll() {
        String expectedQuery = "SELECT id, person_name, person_salary FROM Person;";
        DefaultQueryGenerator queryGenerator = new DefaultQueryGenerator();
        String actualQuery = queryGenerator.findAll(Person.class);
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGenerateFindById() {
        String expectedQuery = "SELECT person_name, person_salary FROM Person WITH id: 1";
        DefaultQueryGenerator queryGenerator = new DefaultQueryGenerator();
        String actualQuery = queryGenerator.findById(Person.class, person.getId());
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testDeleteById() {
        String expectedQuery = "DELETE FROM Person WITH id: 1";
        DefaultQueryGenerator queryGenerator = new DefaultQueryGenerator();
        String actualQuery = queryGenerator.deleteById(Person.class, person.getId());
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testInsert() throws IllegalAccessException {
        String expectedQuery = "INSERT id, person_name, person_salary TO Person WITH values " + person.getId() + ", " + person.getName() + ", " + person.getSalary();
        DefaultQueryGenerator queryGenerator = new DefaultQueryGenerator();
        String actualQuery = queryGenerator.insert(person);
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testUpdate() throws IllegalAccessException, NoSuchFieldException {
        String expectedQuery = "UPDATE person_name, person_salary TO Person WITH id " + person.getId();
        DefaultQueryGenerator queryGenerator = new DefaultQueryGenerator();
        String actualQuery = queryGenerator.update(person);
        assertEquals(expectedQuery, actualQuery);
    }
}
