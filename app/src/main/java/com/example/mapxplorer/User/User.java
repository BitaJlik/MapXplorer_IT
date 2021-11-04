package com.example.mapxplorer.User;

import com.example.mapxplorer.Market;

public class User {
    private String name,pass,email;
    private Market market;
    public User(){}
    public User(String name, String pass, String email) {
        this.name = name;
        this.pass = pass;
        this.email = email;
    }

    public Market getMarket() { return market; }
    public void setMarket(Market market) { this.market = market; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPassword() { return pass; }
    public void setPassword(String pass) { this.pass = pass; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", pass='" + pass + '\'' +
                ", email='" + email + '\'' +
                ", market=" + market +
                '}';
    }
}
