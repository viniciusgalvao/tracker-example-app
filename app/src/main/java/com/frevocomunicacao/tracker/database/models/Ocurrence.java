package com.frevocomunicacao.tracker.database.models;

import java.io.Serializable;

/**
 * Created by Vinicius on 17/03/16.
 */
public class Ocurrence implements Serializable {

    private int id;
    private String name;

    public Ocurrence() {}

    public Ocurrence(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
