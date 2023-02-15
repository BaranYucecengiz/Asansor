package com.by.myapplication;

import java.util.ArrayList;

public class Ustam {
    String user_id;
    String[] elevator_id = {"1", "2", "3", "4"};
    String[] elevator_address = {"A Cad.", "B. Cad", "C. Cad", "D. Cad"};
    ArrayList<Elevators> user_elevators = new ArrayList<>();

    public Ustam(String user_id, String[] elevator_id, String[] elevator_address) {
        this.user_id = user_id;
        this.elevator_id = elevator_id;
        this.elevator_address = elevator_address;
    }


}
