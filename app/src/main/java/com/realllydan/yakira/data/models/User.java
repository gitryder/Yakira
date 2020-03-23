package com.realllydan.yakira.data.models;

public class User {

    public static final String TYPE_ADMIN = "0";
    public static final String TYPE_GENERAL = "1";

    private String name, email, accountType;

    public User() {
    }

    public User(String name, String email, String accountType) {
        this.name = name;
        this.email = email;
        this.accountType = accountType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
