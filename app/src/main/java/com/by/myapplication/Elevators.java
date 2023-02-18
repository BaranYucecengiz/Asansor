package com.by.myapplication;

import java.io.Serializable;
import java.util.ArrayList;

public class Elevators implements Serializable {
    String id, address;
    ArrayList<Parameters> parameters = new ArrayList<>();
    public Elevators(String id, String address) {
        this.id = id;
        this.address = address;
    }
    public void add_Parameters(Parameters ph){
        parameters.add(ph);
    }

    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

}
