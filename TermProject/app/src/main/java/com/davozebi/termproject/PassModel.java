package com.davozebi.termproject;

import java.io.Serializable;

public class PassModel implements Serializable {

    private String fkEmail;
    private String service;
    private String userName;
    private String pass;

    public PassModel(String fkEmail, String service, String userName, String pass) {
        this.fkEmail = fkEmail;
        this.service = service;
        this.userName = userName;
        this.pass = pass;
    }

    public String getFkEmail() {
        return fkEmail;
    }

    public String getService() {
        return service;
    }

    public String getUserName() {
        return userName;
    }

    public String getPass() {
        return pass;
    }
}
