package com.by.myapplication;

public class User {
    private static User instance = null;
    private String id;
    private String password;

    private User(){

    }

    public static User getInstance(){
        if (instance == null){
            instance = new User();
        }
        return instance;
    }

    public void DummyFunction(){
        
    }

}
