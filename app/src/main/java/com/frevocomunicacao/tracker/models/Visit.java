package com.frevocomunicacao.tracker.models;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Visit implements Serializable {

    private int _id, id, employeeId;
    private String motive, cep, state, city,
            address, neighborhood, complement,
            number, referencePoint, observation;

    private Date dateFinish;
    private String[] status = {"Selecione um status", "Aguardando", "Pendente", "Visitado"};

    private List<VisitImages> images;
    private List<Ocurrences> ocurrences;

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

        this.images = new ArrayList<VisitImages>();
        this.ocurrences = new ArrayList<Ocurrences>();
    }

    public int getLocalId() {
        return this._id;
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

    public List<VisitImages> getImages() {
        return this.images;
    }

    public void setImages(List<VisitImages> images) {
        this.images = images;
    }

    public void addImage(VisitImages object) {
        this.images.add(object);
    }

    public List<Ocurrences> getOcurrences() {
        return this.ocurrences;
    }

    public void setOcurrences(List<Ocurrences> ocurrences) {
        this.ocurrences = ocurrences;
    }

    public void addOcurrence(Ocurrences object) {
        this.ocurrences.add(object);
    }
}