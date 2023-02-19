package com.by.myapplication;

import java.io.Serializable;
import java.util.ArrayList;

public class Users implements Serializable {

    private static Users instance = null;
    String user_id;
    String user_password;
    ArrayList<Elevator> user_elevators = new ArrayList<>();

    public static synchronized Users getInstance(){
        if(instance == null)
            instance = new Users();
        return instance;
    }
    public Users(){

    }
    public Users(String user_id, String password) {
        this.user_id = user_id;
        this.user_password = password;
    }
    public void addElevator(Elevator e){
        user_elevators.add(e);
    }

    public ArrayList<Elevator> getUser_elevators(){
        return user_elevators;
    }
    public static void setInstance(Users instance) {
        Users.instance = instance;
    }
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public void setUser_password(String password) {
        this.user_password = password;
    }

    public void setUser_elevators(ArrayList<Elevator> user_elevators) {
        this.user_elevators = user_elevators;
    }
}
