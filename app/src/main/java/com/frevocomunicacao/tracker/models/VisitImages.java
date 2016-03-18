package com.frevocomunicacao.tracker.models;

import java.io.Serializable;

public class VisitImages implements Serializable {

    private int _id;
    private String name, source;

    public VisitImages(String name, String source) {
        this.name = name;
        this.source = source;
    }

    public int getLocalId() {
        return _id;
    }

    public void setLocalId(int _id) {
        this._id = _id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
