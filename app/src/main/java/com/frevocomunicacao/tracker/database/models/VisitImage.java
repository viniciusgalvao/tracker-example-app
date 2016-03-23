package com.frevocomunicacao.tracker.database.models;

import java.io.Serializable;

public class VisitImage implements Serializable {

    private int id, visit_id;
    private String name, source;

    public VisitImage() {}

    public VisitImage(String name, String source) {
        this.name = name;
        this.source = source;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVisitId() {
        return this.visit_id;
    }

    public void setVisitId(int visit_id) {
        this.visit_id = visit_id;
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
