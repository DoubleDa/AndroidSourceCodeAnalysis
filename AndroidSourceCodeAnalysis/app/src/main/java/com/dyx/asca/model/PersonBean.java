package com.dyx.asca.model;

import java.io.Serializable;

/**
 * Author：dayongxin
 * Function：
 */
public class PersonBean implements Serializable {
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
