package com.dyx.asca.model.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Author：dayongxin
 * Function：
 */
public class Person extends RealmObject {

    public Person() {
    }

    public Person(String name) {
        this.name = name;
    }

    private String name;
    private int age;
    private String company;

    @PrimaryKey
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
