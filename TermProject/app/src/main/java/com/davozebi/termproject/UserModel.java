package com.davozebi.termproject;

import java.io.Serializable;

public class UserModel implements Serializable {

    private String email;
    private String pass;

    public UserModel(String email, String pass) {
        this.email = email;
        this.pass = pass;
    }

    public String getEmail() {
        return email;
    }

    public String getPass() {
        return pass;
    }
}
