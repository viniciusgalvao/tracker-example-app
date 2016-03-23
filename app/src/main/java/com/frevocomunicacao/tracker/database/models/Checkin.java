package com.frevocomunicacao.tracker.database.models;

import java.io.Serializable;

public class Checkin implements Serializable {

    private int _id,
            id,
            visit_id,
            visit_status_id,
            employee_id;

    private double latitude, longitude;
    private String observation, date, hour;

    public Checkin() {}

    public Checkin(int id, int visit_id, int visit_status_id, int employee_id, double latitude, double longitude, String observation, String date, String hour) {
        this.id             = id;
        this.visit_id       = visit_id;
        this.visit_status_id= visit_status_id;
        this.employee_id    = employee_id;
        this.latitude       = latitude;
        this.longitude      = longitude;
        this.observation    = observation;
        this.date           = date;
        this.hour           = hour;
    }

    public int getLocalId() {
        return _id;
    }

    public void setLocalId(int _id) {
        this._id = _id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVisitId() {
        return visit_id;
    }

    public void setVisitId(int visit_id) {
        this.visit_id = visit_id;
    }

    public int getVisitStatusId() {
        return visit_status_id;
    }

    public void setVisitStatusId(int visit_status_id) {
        this.visit_status_id = visit_status_id;
    }

    public int getEmployeeId() {
        return employee_id;
    }

    public void setEmployeeId(int employee_id) {
        this.employee_id = employee_id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }
}
