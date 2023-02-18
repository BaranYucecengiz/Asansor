package com.by.myapplication;

import java.io.Serializable;
import java.util.ArrayList;

public class Users implements Serializable {
    String user_id;
    ArrayList<Elevators> user_elevators = new ArrayList<>();

    public Users(String user_id) {
        this.user_id = user_id;

    }
    public void addElevator(Elevators e){
        user_elevators.add(e);
    }

    public ArrayList<Elevators> getUser_elevators() {
        return user_elevators;
    }
}
