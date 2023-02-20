package com.by.myapplication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class Elevator implements Serializable {
    private String id;
    private String owner_id;
    private String address;
    private String setupDate;
    private String lastConfigurationDate;
    private String communicationNumber;

    private Map<String, Integer> P;
    private Map<String, Integer> D;
    private Map<String, Integer> N;

    //CTors
    public Elevator(String id, String address) {
        this.id = id;
        this.address = address;
    }

    // Getters and Setters
    public Elevator(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner_id;
    }

    public void setOwner(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSetupDate() {
        return setupDate;
    }

    public void setSetupDate(String setupDate) {
        this.setupDate = setupDate;
    }

    public String getLastConfigurationDate() {
        return lastConfigurationDate;
    }

    public void setLastConfigurationDate(String lastConfigurationDate) {
        this.lastConfigurationDate = lastConfigurationDate;
    }

    public String getCommunicationNumber() {
        return communicationNumber;
    }

    public void setCommunicationNumber(String communicationNumber) {
        this.communicationNumber = communicationNumber;
    }

    public Map<String, Integer> getP() {
        return P;
    }

    public void setP(Map<String, Integer> p) {
        P = p;
    }

    public Map<String, Integer> getD() {
        return D;
    }

    public void setD(Map<String, Integer> d) {
        D = d;
    }

    public Map<String, Integer> getN() {
        return N;
    }

    public void setN(Map<String, Integer> n) {
        N = n;
    }

    public Map<String, Integer> getElevator(String code){
        if(code == "P")
            return P;
        else if (code == "D")
            return D;
        else
            return N;
    }
}
