package com.frevocomunicacao.tracker.models;

import java.io.Serializable;

/**
 * Created by Vinicius on 17/03/16.
 */
public class Ocurrences implements Serializable {

    private int _id, id;
    private String name;

    public Ocurrences(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setLocalId(int _id) {
        this._id = _id;
    }

    public int getLocalId(int _id) {
        return this._id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
