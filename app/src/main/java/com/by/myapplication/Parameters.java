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

    public Parameters(String code, String sub_code, String description, String type, float value, int minRange, int maxRange, ArrayList<String> parameters_name) {
        this.code = code;
        this.sub_code = sub_code;
        this.description = description;
        this.type = type;
        this.value = value;
        this.minRange = minRange;
        this.maxRange = maxRange;
        this.parameters_name = parameters_name;
    }

    public void add_parameters_name(String parameter){
        parameters_name.add(parameter);
    }

    public String getCode() {
        return code;
    }

    public String getSub_code() {
        return sub_code;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public float getValue() {
        return value;
    }

    public int getMinRange() {
        return minRange;
    }

    public int getMaxRange() {
        return maxRange;
    }

    public ArrayList<String> getParameters_name() {
        return parameters_name;
    }

}
