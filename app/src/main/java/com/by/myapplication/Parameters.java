package com.by.myapplication;

import java.io.Serializable;
import java.util.ArrayList;

public class Parameters implements Serializable {
    String code;
    String sub_code;
    String description;
    String type;
    float value;
    int minRange, maxRange;
    ArrayList<String> parameters_name = new ArrayList<>();

    public Parameters(String code, String sub_code, String description, String type, float value, int minRange, int maxRange) {
        this.code = code;
        this.sub_code = sub_code;
        this.description = description;
        this.type = type;
        this.value = value;
        this.minRange = minRange;
        this.maxRange = maxRange;
    }

    public void add_parameters_name(String parameter){
        parameters_name.add(parameter);
    }
}
