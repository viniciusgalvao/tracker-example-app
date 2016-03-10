package com.frevocomunicacao.tracker.models;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Visit implements Serializable {

    private int id, employeeId;
    private String motive, cep, state, city,
            address, neighborhood, complement,
            number, referencePoint, observation;

    private Date dateFinish;
    private String[] status = {"Selecione um status", "Aguardando", "Pendente", "Visitado"};

    public Visit() {}

    public Visit(int id, int employeeId, String motive, String cep, String address, String number, String complement, String neighborhood, String city, String state, String referencePoint) {
        super();
        this.id             = id;
        this.employeeId     = employeeId;
        this.motive         = motive;
        this.cep            = cep.equals("") ? "sem cep" : cep;
        this.address        = address;
        this.number         = number.equals("") ? "S/N": number;
        this.complement     = complement;
        this.neighborhood   = neighborhood;
        this.city           = city;
        this.state          = state;
        this.referencePoint = referencePoint;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmplyeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getMotive() {
        return motive;
    }

    public void setMotive(String motive) {
        this.motive = motive;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getReferencePoint() {
        return referencePoint;
    }

    public void setReferencePoint(String referencePoint) {
        this.referencePoint = referencePoint;
    }

    public String[] getStatus() {
        return status;
    }

    public void setDateFinish(Date date) {
        this.dateFinish = date;
    }

    public String getDateFinish(){
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(this.dateFinish);
    }

    private void setStatus(String[] status) {
        this.status = status;
    }

    public String getSimpleAddress() {
        return this.address + ", " + this.number + " - " + this.neighborhood;
    }

    private void setObservation(String observation) {
        this.observation = observation;
    }

    public String getObservation() {
        return this.observation;
    }
}