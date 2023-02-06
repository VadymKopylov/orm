package com.kopylov.orm.entity;

import com.kopylov.orm.annotation.Column;
import com.kopylov.orm.annotation.Table;

@Table
public class Person {
    @Column
    private int id;

    @Column(name = "person_name")
    private String name;

    @Column(name = "person_salary")
    private double salary;

    public Person(int id, String name, double salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }
}
