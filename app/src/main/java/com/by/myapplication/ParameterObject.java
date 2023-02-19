package com.by.myapplication;

import java.util.Map;
import java.util.Objects;

class EnumObject {
    private String description_eng;
    private String description_tr;

    public EnumObject() {

    }

    public EnumObject(String description_eng, String description_tr) {
        this.description_eng = description_eng;
        this.description_tr = description_tr;
    }

    public String getDescription_eng() {
        return description_eng;
    }

    public void setDescription_eng(String description_eng) {
        this.description_eng = description_eng;
    }

    public String getDescription_tr() {
        return description_tr;
    }

    public void setDescription_tr(String description_tr) {
        this.description_tr = description_tr;
    }
}

class PDNParameter {
    private String code;
    private String description_eng;
    private String description_tr;
    private String type;
    private Long default_val;
    private Long range_start;
    private Long range_end;
    private Map<String, EnumObject> enums;

    public PDNParameter() {
    }

    public PDNParameter(String code, String description_eng, String description_tr, String type, Long default_val, Long range_start, Long range_end, Map<String, EnumObject> enums) {
        this.code = code;
        this.description_eng = description_eng;
        this.description_tr = description_tr;
        this.type = type;
        this.default_val = default_val;
        this.range_start = range_start;
        this.range_end = range_end;
        this.enums = enums;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription_eng() {
        return description_eng;
    }

    public void setDescription_eng(String description_eng) {
        this.description_eng = description_eng;
    }

    public String getDescription_tr() {
        return description_tr;
    }

    public void setDescription_tr(String description_tr) {
        this.description_tr = description_tr;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getDefault_val() {
        return default_val;
    }

    public void setDefault_val(Long default_val) {
        this.default_val = default_val;
    }

    public Long getRange_start() {
        return range_start;
    }

    public void setRange_start(Long range_start) {
        this.range_start = range_start;
    }

    public Long getRange_end() {
        return range_end;
    }

    public void setRange_end(Long range_end) {
        this.range_end = range_end;
    }

    public Map<String, EnumObject> getEnums() {
        return enums;
    }

    public void setEnums(Map<String, EnumObject> enums) {
        this.enums = enums;
    }
}

public class ParameterObject {
    private Map<String, PDNParameter> P;
    private Map<String, PDNParameter> D;
    private Map<String, PDNParameter> N;

    public ParameterObject() {
    }

    public ParameterObject(Map<String, PDNParameter> p, Map<String, PDNParameter> d, Map<String, PDNParameter> n) {
        P = p;
        D = d;
        N = n;
    }

    public Map<String, PDNParameter> getP() {
        return P;
    }

    public void setP(Map<String, PDNParameter> p) {
        P = p;
    }

    public Map<String, PDNParameter> getD() {
        return D;
    }

    public void setD(Map<String, PDNParameter> d) {
        D = d;
    }

    public Map<String, PDNParameter> getN() {
        return N;
    }

    public void setN(Map<String, PDNParameter> n) {
        N = n;
    }

    public void setPDN(Map<String, PDNParameter> PDNMap, String PDN){
        if (Objects.equals(PDN, "P")) this.setP(PDNMap);
        if (Objects.equals(PDN, "D")) this.setD(PDNMap);
        if (Objects.equals(PDN, "N")) this.setN(PDNMap);
    }

}
